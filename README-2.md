# 9. Response (RunResponse)
Example

{
"finalState": {
"x": 1,
"y": 1,
"z": 1,
"direction": "UP"
},
"visited": [
"(0,0,0)",
"(0,1,0)",
"(1,1,0)",
"(1,1,1)"
],
"execution": {
"totalCommands": 5,
"executedCommands": 5,
"blockedMoves": 0,
"invalidCommands": [
{
"index": 3,
"command": "X",
"reason": "Unknown command"
}
]
},
"grid": {
"width": 5,
"height": 5,
"depth": 5
},
"summary": "Visited 4 positions on a 5x5x5 grid. Final position: (1,1,1) facing UP. Total commands: 5, executed: 5, blocked moves: 0, invalid commands: 1."
}


summary field
Human-readable summary containing:
•	Number of positions visited
•	Grid size
•	Final position & direction
•	Total/executed/blocked/invalid commands counts!

# 10. Error Handling

| Condition                    | HTTP Status | Example                             |
|------------------------------|-------------|-------------------------------------|
| Start position out of bounds | `422`       | `"Start position is out of bounds"` |
| Invalid grid dimensions      | `422`       | `"Width must be at least 1"`        |
| Invalid obstacle position    | `500`       | `"Obstacle out of bounds"`          |
| Malformed JSON               | `400`       | `"Bad Request"`                     |
| Internal errors              | `500`       | `"Internal Server Error"`           |

# 11. Running the Application

Run tests       - mvn clean test
Run application - mvn spring-boot:run

The API will be available at: http://localhost:8080/api/probe/run

# 12. TDD Approach Summary

This project follows a TDD-style workflow:
   1.	Write failing test
   2.	Write minimal implementation
   3.	Refactor with tests remaining green
   4.	Commit after each step with clear messages:
      o	test: ...
      o	feat: ...
      o	refactor: ...
      o	docs: ...

# 13. Folder Structure

src
├── main
│   ├── java/com/natwest/kata/submersible
│   │     ├── api (controllers, DTOs)
│   │     ├── domain (Grid, Probe, Direction)
│   │     ├── interpreter (CommandInterpreter, ExecutionResult)
│   │     └── service (ProbeRunService)
│   └── resources
└── test
├── api
├── domain
├── interpreter
└── service




