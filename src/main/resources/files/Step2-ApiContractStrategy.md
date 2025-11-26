Step 2: API contract for the stateless probe control service.
We’ll define a single primary endpoint POST /api/probe/run that receives the entire scenario (grid, obstacles, start, direction, commands), executes it, and returns an execution report.
________________________________________________________________________________
1) Endpoint Summary
POST /api/probe/run
Executes a batch of commands for a probe on a given grid, respecting bounds/obstacles, and returns the final state plus a summary.
•	Consumes: application/json
•	Produces: application/json
•	Idempotent: Yes (same request → same response)
________________________________________________________________________________
2) Command Alphabet
•	F = Move Forward (in current facing direction)
•	B = Move Backward (opposite of facing direction)
•	L = Turn Left (90°)
•	R = Turn Right (90°)
•	U = Move Up (+z)
•	D = Move Down (−z)
Any character outside {F,B,L,R,U,D} is considered invalid. Invalid commands do not change state and are recorded in the response.
________________________________________________________________________________
3) Request DTO (JSON)

{
  "grid": {
    "width": 6,
    "height": 6,
    "depth": 6
  },
  "obstacles": [
    { "x": 3, "y": 3, "z": 5 },
    { "x": 0, "y": 1, "z": 0 }
  ],
  "start": { "x": 2, "y": 3, "z": 5 },
  "direction": "NORTH",
  "commands": ["F", "F", "R", "B", "U", "D"]
}

Validation Rules
•	grid.width, grid.height, grid.depth > 0
•	All obstacle coordinates must be within bounds (0 ≤ x < width, 0 ≤ y < height, 0 ≤ z < depth)
•	start must be within bounds
•	direction ∈ {NORTH, SOUTH, EAST, WEST}
•	commands may be empty (no movement), but if present, each must be one of {F,B,L,R,U,D}; invalids will be reported (HTTP 200, with details) unless you prefer HTTP 400 (configurable—see Error Model).

________________________________________________________________________________
4) Response DTO (JSON)


{
  "finalState": {
    "x": 3,
    "y": 4,
    "z": 5,
    "direction": "EAST"
  },
  "visited": [
    "(2,3,5)",
    "(2,4,5)",
    "(3,4,5)"
  ],
  "execution": {
    "totalCommands": 6,
    "executedCommands": 6,
    "blockedMoves": 1,
    "invalidCommands": [
      { "index": 4, "command": "X", "reason": "UNKNOWN_COMMAND" }
    ]
  },
  "grid": {
    "width": 6,
    "height": 6,
    "depth": 6
  }
}

Semantics
•	finalState: The probe’s position and facing direction after executing the batch.
•	visited: Coordinates visited including the starting position; only successful moves append new coordinates. Format: "(x,y,z)".
•	execution.totalCommands: Length of commands in request.
•	execution.executedCommands: Number of commands processed (always equals totalCommands; even invalids are “processed” but don’t change state).
•	execution.blockedMoves: Count of movement commands (F/B/U/D) that were blocked due to bounds or obstacles (state unchanged for those steps).
•	execution.invalidCommands: List of invalid entries (index from 0-based list position, original command, and reason).

________________________________________________________________________________
5) Error Model
Error Envelope (for 4xx/5xx)

{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Grid dimensions must be positive",
    "details": [
      { "field": "grid.width", "issue": "MUST_BE_POSITIVE" },
      { "field": "start", "issue": "OUT_OF_BOUNDS" }
    ],
    "traceId": "b1f2b3c4d5"
  }
}

Common Errors
•	400 Bad Request
o	Invalid payload shape or types.
o	direction not in {NORTH,SOUTH,EAST,WEST} (enum violation).
o	Command list contains only invalid commands and policy is strict.
•	422 Unprocessable Entity
o	start out of bounds.
o	One or more obstacles out of bounds.
•	500 Internal Server Error
o	Unhandled exceptions (should be rare; log with traceId).

________________________________________________________________________________
6) OpenAPI (Swagger) – Contract Snippet

This is an illustrative subset. We’ll generate a full spec as we implement controllers and DTOs.


openapi: 3.0.3
info:
  title: Submersible Probe API
  version: 1.0.0
paths:
  /api/probe/run:
    post:
      summary: Execute a batch of commands on a probe in a given grid
      operationId: runProbe
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/RunRequest'
      responses:
        '200':
          description: Execution report with final state and visited coordinates
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/RunResponse'
        '400':
          description: Bad request (validation or invalid enum/commands policy)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
        '422':
          description: Unprocessable entity (out-of-bounds start/obstacles)
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ErrorResponse'
components:
  schemas:
    Coordinate:
      type: object
      properties:
        x: { type: integer, minimum: 0 }
        y: { type: integer, minimum: 0 }
        z: { type: integer, minimum: 0 }
      required: [x, y, z]
    Grid:
      type: object
      properties:
        width:  { type: integer, minimum: 1 }
        height: { type: integer, minimum: 1 }
        depth:  { type: integer, minimum: 1 }
      required: [width, height, depth]
    RunRequest:
      type: object
      properties:
        grid: { $ref: '#/components/schemas/Grid' }
        obstacles:
          type: array
          items: { $ref: '#/components/schemas/Coordinate' }
          default: []
        start: { $ref: '#/components/schemas/Coordinate' }
        direction:
          type: string
          enum: [NORTH, SOUTH, EAST, WEST]
        commands:
          type: array
          items:
            type: string
            maxLength: 1
            pattern: "^[FBRLUD]$"
      required: [grid, start, direction, commands]
    FinalState:
      type: object
      properties:
        x: { type: integer }
        y: { type: integer }
        z: { type: integer }
        direction:
          type: string
          enum: [NORTH, SOUTH, EAST, WEST]
      required: [x, y, z, direction]
    Execution:
      type: object
      properties:
        totalCommands:    { type: integer }
        executedCommands: { type: integer }
        blockedMoves:     { type: integer }
        invalidCommands:
          type: array
          items:
            type: object
            properties:
              index:   { type: integer }
              command: { type: string }
              reason:  { type: string, enum: [UNKNOWN_COMMAND] }
    RunResponse:
      type: object
      properties:
        finalState: { $ref: '#/components/schemas/FinalState' }
        visited:
          type: array
          items: { type: string, pattern: "^\\([0-9]+,[0-9]+,[0-9]+\\)$" }
        execution: { $ref: '#/components/schemas/Execution' }
        grid: { $ref: '#/components/schemas/Grid' }
      required: [finalState, visited, execution]
    ErrorResponse:
      type: object
      properties:
        error:
          type: object
          properties:
            code:    { type: string }
            message: { type: string }
            details:
              type: array
              items:
                type: object
                properties:
                  field: { type: string }
                  issue: { type: string }
            traceId: { type: string }
      required: [error]

________________________________________________________________________________
7) Example Requests & Responses
Example 1 — Normal execution, no invalid commands

Request
POST /api/probe/run
{
  "grid": { "width": 6, "height": 6, "depth": 6 },
  "obstacles": [{ "x": 3, "y": 3, "z": 5 }],
  "start": { "x": 2, "y": 3, "z": 5 },
  "direction": "EAST",
  "commands": ["F", "F", "U", "D", "L", "B"]
}

Response (200)
{
  "finalState": { "x": 3, "y": 3, "z": 5, "direction": "NORTH" },
  "visited": ["(2,3,5)", "(3,3,5)", "(3,3,5)", "(3,3,5)"],
  "execution": {
    "totalCommands": 6,
    "executedCommands": 6,
    "blockedMoves": 1,
    "invalidCommands": []
  },
  "grid": { "width": 6, "height": 6, "depth": 6 }
}

Note: The second F is blocked by obstacle (3,3,5) so visited doesn’t grow there;
blockedMoves = 1. (Depending on your preference, you may or may not include duplicates
for non-moves; current domain records only successful moves,
so visited would be "(2,3,5)", "(3,3,5)" in your current implementation—let’s keep that consistent.)

-------------
Example 2 — Invalid command present
Request
{
  "grid": { "width": 5, "height": 5, "depth": 5 },
  "obstacles": [],
  "start": { "x": 0, "y": 0, "z": 0 },
  "direction": "NORTH",
  "commands": ["F", "X", "R", "B"]
}

Response (200)

{
  "finalState": { "x": 1, "y": 1, "z": 0, "direction": "EAST" },
  "visited": ["(0,0,0)", "(0,1,0)", "(1,1,0)"],
  "execution": {
    "totalCommands": 4,
    "executedCommands": 4,
    "blockedMoves": 0,
    "invalidCommands": [
      { "index": 1, "command": "X", "reason": "UNKNOWN_COMMAND" }
    ]
  },
  "grid": { "width": 5, "height": 5, "depth": 5 }
}
----------------
Example 3 — Validation error (start out-of-bounds)
Response (422)

{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Start position is out of bounds",
    "details": [
      { "field": "start", "issue": "OUT_OF_BOUNDS" }
    ],
    "traceId": "a9c1f5e2"
  }
}

________________________________________________________________________________
8) Design Choices to Confirm
•	Invalid command policy: Continue and report (200) or reject with 400?
Proposed: Continue and report—keeps API ergonomic.
•	Blocked moves reporting: Count only movement commands that attempt to change coords and fail due to bounds/obstacles.
Proposed: Yes; turning commands are never blocked.
•	Visited list semantics: Include starting position; append only on successful moves.
Proposed: Match your current Probe implementation.
•	Direction enum: Fixed to {NORTH,SOUTH,EAST,WEST}.

________________________________________________________________________________





