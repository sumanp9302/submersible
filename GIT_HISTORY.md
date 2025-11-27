# TDD Git History (Narrative Log)

> This file describes the intended TDD-style evolution of the submersible probe API.
> It is a human-readable summary of commits, not the actual `git log` output.

---

## 1. Project bootstrap

**Commit:** `chore: bootstrap Spring Boot probe API`

- Initialize Spring Boot project structure.
- Add `SubmersibleApplication` entry point.
- Add basic Maven configuration (`pom.xml`) with:
    - Spring Web
    - Validation
    - Test dependencies (JUnit 5, Spring Boot Test)

---

## 2. First domain spec – Grid bounds

**Commit:** `test: introduce GridTest for 3D bounds`

- Add `GridTest` as the first domain-level specification.
- Define expectations for:
    - Valid coordinates being inside bounds
    - Out-of-bounds coordinates being rejected
- No production implementation yet (tests initially fail).

---

## 3. Implement 3D grid with obstacle support

**Commit:** `feat: implement Grid with bounds and obstacles`

- Implement `Grid` domain type with:
    - `width`, `height`, `depth`
    - `isWithinBounds(x,y,z)`
    - `addObstacle(x,y,z)` and `isObstacle(x,y,z)`
- Store obstacles as `Set<String>` to naturally deduplicate duplicates.
- Make `GridTest` pass.

---

## 4. Probe movement spec

**Commit:** `test: define probe movement and turning behaviour`

- Add `ProbeTest` with scenarios for:
    - Initializing a probe at (x,y,z) with a direction.
    - Turning left/right/up/down from different initial orientations.
    - Moving forwards/backwards while respecting bounds and obstacles.
    - Tracking visited coordinates over time.
- Tests initially fail until `Probe` is implemented.

---

## 5. Implement Probe domain

**Commit:** `feat: implement Probe with movement and visited tracking`

- Implement `Probe` domain object:
    - Encapsulate current (x,y,z) and `Direction`.
    - Implement `moveForward`, `moveBackward`, `turnLeft`, `turnRight`, `turnUp`, `turnDown`.
    - Use `Grid` to prevent out-of-bounds and obstacle collisions.
    - Record all visited coordinates (including starting position).
- Make `ProbeTest` pass.

---

## 6. Command interpreter spec

**Commit:** `test: define interpreter behaviour for valid and invalid commands`

- Add `CommandInterpreterTest` describing:
    - Handling of valid commands: `F`, `B`, `L`, `R`, `U`, `D`.
    - Behaviour when encountering invalid commands (unknown symbols, multi-character tokens, `null`).
    - Aggregation of execution metrics:
        - total commands
        - executed commands
        - blocked moves
        - invalid commands list
- Tests initially fail.

---

## 7. Implement CommandInterpreter and ExecutionResult

**Commit:** `feat: implement CommandInterpreter and execution result model`

- Implement `CommandInterpreter`:
    - Interpret each command string and call appropriate `Probe` methods.
    - Count `totalCommands`, `executedCommands`, `blockedMoves`.
    - Record invalid commands in an `InvalidCommand` / `InvalidCommandDto` list.
- Introduce `ExecutionResult` as an internal summary type.
- Make all `CommandInterpreterTest` cases pass.

---

## 8. Service-layer orchestration spec

**Commit:** `test: describe ProbeRunService orchestration from request to response`

- Add `ProbeRunServiceTest` to specify:
    - How `RunRequest` is transformed into domain objects (`Grid`, `Probe`).
    - How `CommandInterpreter` is used.
    - What `RunResponse` should contain:
        - `finalState`
        - `visited`
        - `execution` metrics
        - textual `summary`
- Tests start as failing until service is wired up.

---

## 9. Implement ProbeRunService

**Commit:** `feat: implement ProbeRunService and RunResponse mapping`

- Implement `ProbeRunService.run(RunRequest)`:
    - Build `Grid` from `GridDto` and add obstacles.
    - Validate start position is within bounds.
    - Create `Probe` using the domain model.
    - Call `CommandInterpreter` to execute commands.
    - Map final probe state + execution metrics + visited coordinates into `RunResponse`.
    - Build a human-readable execution summary string.
- Make `ProbeRunServiceTest` pass.

---

## 10. Controller happy-path spec

**Commit:** `test: cover /api/probe/run happy path`

- Add `ProbeControllerTest` with Spring `@WebMvcTest`:
    - POST `/api/probe/run` with a valid `RunRequest`.
    - Assert HTTP 200.
    - Assert `finalState`, `visited`, and `execution` are present and consistent.
    - Assert `summary` is non-empty.
- Tests fail until controller is implemented.

---

## 11. Implement ProbeController

**Commit:** `feat: expose /api/probe/run REST endpoint`

- Implement `ProbeController`:
    - `POST /api/probe/run` accepting a `@Valid RunRequest`.
    - Delegate to `ProbeRunService`.
    - Return `RunResponse` with HTTP 200.
- Make the controller happy-path test pass.

---

## 12. Global error handling spec

**Commit:** `test: define API behaviour for invalid requests`

- Extend `ProbeControllerTest` to cover:
    - Out-of-bounds start coordinates resulting in an error response.
    - Validation and domain errors returning:
        - an `error.code` of `"VALIDATION_ERROR"`
        - an `error.message` describing the issue.
- Tests initially fail.

---

## 13. Implement ErrorResponse and GlobalExceptionHandler

**Commit:** `feat: add structured error responses and exception mapping`

- Add `ErrorResponse` with:
    - `ErrorBody` (code, message, details, traceId).
    - `ErrorDetail` (field, issue).
    - A helper factory `ErrorResponse.validation(...)`.
- Add `GlobalExceptionHandler` with handlers for:
    - `MethodArgumentNotValidException` (e.g. missing required fields) → 422
    - `IllegalArgumentException` (business validation) → 422
    - `ConstraintViolationException` → 400 with field-level details
    - `HttpMessageNotReadableException` (malformed JSON) → 400
- Make error-path controller tests pass.

---

## 14. Edge case spec – empty commands

**Commit:** `test: document behaviour for empty command list`

- Add a service-level test:
    - `commands = []` keeps the probe at the initial coordinates.
    - `ExecutionDto` has all zeros for totals.
- No production code change required (test characterises existing correct behaviour).

---

## 15. Edge case spec – start position on obstacle

**Commit:** `test: define behaviour when start position is an obstacle`

- Add a service-level test for:
    - Starting on a cell that is also an obstacle.
    - Expectation: service should fail fast with `IllegalArgumentException`.
- Test initially fails.

---

## 16. Implement start-on-obstacle fast-fail

**Commit:** `feat: fail fast when start position is an obstacle`

- Update `ProbeRunService.run(...)`:
    - After bounds check, call `grid.isObstacle(startX, startY, startZ)`.
    - If true, throw `IllegalArgumentException("Start position cannot be an obstacle")`.
- All edge-case tests now pass.

---

## 17. Edge case spec – duplicate obstacles and large command list

**Commit:** `test: cover duplicate obstacles and large command sequences`

- Extend `ProbeRunServiceTest` with:
    - A test that compares runs with a single obstacle vs duplicate entries of the same obstacle and asserts identical results.
    - A “large input” sanity test that:
        - Uses a reasonably large grid (e.g. 100×100×100).
        - Executes a long command list (e.g. 10,000 `F` commands).
        - Asserts that the run completes and remains within bounds.

---

## 18. Controller error-path specs for malformed and invalid input

**Commit:** `test: add controller tests for malformed JSON and missing fields`

- Extend `ProbeControllerTest` with:
    - A test for malformed JSON body where the request is not parseable:
        - Expect HTTP 400.
        - `error.code = "VALIDATION_ERROR"`.
        - `error.message = "Malformed JSON request"`.
    - A test for a request missing mandatory fields (e.g. `grid` is `null`):
        - Expect HTTP 422.
        - `error.code = "VALIDATION_ERROR"`.
        - `error.message` is non-empty and helpful.

---

## 19. Documentation of TDD history and performance scope

**Commit:** `docs: add TDD git history and performance notes`

- Add `GIT_HISTORY.md` describing the TDD evolution of the project.
- Update `README` with a short note explaining:
    - Performance testing is limited to a large-input sanity test.
    - This is acceptable for a kata but could be extended in a real system.
