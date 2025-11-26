# Submersible Probe Control API

This project implements a REST API to remotely control a submersible probe navigating a 3D underwater grid.  
The goal is to process a sequence of movement commands, maintain probe state, avoid obstacles, and return a full execution summary.

---

# 1. Problem Overview

A submersible probe explores the ocean floor using a remotely-controlled command interface.

The system must:
- Execute commands (F/B/L/R/U/D)
- Stay inside grid boundaries
- Avoid obstacles
- Track and return all coordinates visited
- Return final probe state and an execution summary
- Handle invalid commands gracefully
- Be test-driven and implemented using clean code principles

---

# 2. Technologies Used

- **Java 21**
- **Spring Boot**
- **Spring MVC**
- **JUnit 5**
- **MockMvc**
- **Maven**

---

# 3. Architecture Overview

### Layers:
- **API Layer** – Controllers & DTOs for REST contract
- **Service Layer** – Orchestrates grid, probe, interpreter
- **Domain Layer** – Core business logic (`Grid`, `Probe`, `Direction`)
- **Interpreter** – Processes commands and tracks execution results
- **Tests** – TDD-style validation of domain, API, interpreter behavior

---

# 4. Coordinate System & Grid

The ocean floor is a **3D grid** defined by:

| Axis | Meaning | Range             |
|------|---------|-------------------|
| x    | Width   | `0 .. width - 1`  |
| y    | Height  | `0 .. height - 1` |
| z    | Depth   | `0 .. depth - 1`  |

The probe starts at `(x, y, z)` facing a direction.

A position is **valid** if:
    0 ≤ x < width
    0 ≤ y < height
    0 ≤ z < depth


Any move outside these bounds is **blocked**.

Obstacles are also located at `(x, y, z)` coordinates.

---

# 5. Command Set (Explicit API Contract)

The API supports the following **commands**:

| Command | Meaning    | Behavior                                                      |
|---------|------------|---------------------------------------------------------------|
| `F`     | Forward    | Move in the current direction                                 |
| `B`     | Backward   | Move opposite to current direction                            |
| `L`     | Turn Left  | 90° left (horizontal only: N ↔ W ↔ S ↔ E)                     |
| `R`     | Turn Right | 90° right (horizontal only: N ↔ E ↔ S ↔ W)                    |
| `U`     | Turn Up    | Switch to vertical UP direction (remembers last horizontal)   |
| `D`     | Turn Down  | Switch to vertical DOWN direction (remembers last horizontal) |

Invalid commands are allowed in the request but:
- They do **not** break the execution
- They are skipped
- They are returned in the `invalidCommands` list with index + reason

---

# 6. Direction Rules

### Horizontal Directions
- `NORTH`, `SOUTH`, `EAST`, `WEST`

`L` and `R` only work in these directions.

### Vertical Directions
- `UP`, `DOWN`

While facing UP/DOWN:
- `L` and `R` do nothing
- `U`/`D` may restore the last horizontal direction

### Movement Rules

Movement always applies **relative to current direction**:

| Facing | Forward (F)   | Backward (B)  |
|--------|---------------|---------------|
| NORTH  | `(x, y+1, z)` | `(x, y-1, z)` |
| SOUTH  | `(x, y-1, z)` | `(x, y+1, z)` |
| EAST   | `(x+1, y, z)` | `(x-1, y, z)` |
| WEST   | `(x-1, y, z)` | `(x+1, y, z)` |
| UP     | `(x, y, z+1)` | `(x, y, z-1)` |
| DOWN   | `(x, y, z-1)` | `(x, y, z+1)` |

Moves blocked due to **boundaries** or **obstacles** increase `blockedMoves`.

---

# 7. Obstacles

Obstacles are cells where the probe cannot enter.

If the probe attempts to move into an obstacle:
- Move is blocked
- Position does not change
- `blockedMoves` increments
- Execution continues normally

Obstacles outside the grid trigger an error: 
    IllegalArgumentException: Obstacle out of bounds

---

# 8. REST API

## Endpoint

## Request Body (`RunRequest`)

```json jkk
{
  "grid": {
    "width": 5,
    "height": 5,
    "depth": 5
  },
  "start": {
    "x": 0,
    "y": 0,
    "z": 0
  },
  "direction": "NORTH",
  "commands": ["F", "R", "F", "U", "F"],
  "obstacles": [
    { "x": 1, "y": 0, "z": 0 },
    { "x": 2, "y": 2, "z": 1 }
  ]
}

Request fields
_______________________________________________________________________________________________
| Field       | Required | Description                                                        |
| ----------- | -------- | ------------------------------------------------------------------ |
| `grid`      | Yes      | The 3D grid dimensions                                             |
| `start`     | Yes      | Starting coordinates of the probe                                  |
| `direction` | Yes      | Initial direction (`NORTH`, `SOUTH`, `EAST`, `WEST`, `UP`, `DOWN`) |
| `commands`  | Yes      | List of command strings                                            |
| `obstacles` | No       | Coordinates where movement is blocked                              |
-----------------------------------------------------------------------------------------------

