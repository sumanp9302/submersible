# TDD Git History (Narrative Summary)

This document describes the intended TDD-style evolution of the Submersible Probe
Control API. It is a human-readable summary of the key commits, not a literal
`git log`.

Each step follows the basic TDD loop: **RED → GREEN → REFACTOR**, with small,
focused commits.

---

## 1. Project Bootstrap

**Commit:** `chore: bootstrap Spring Boot probe API`

- Initialize Spring Boot project structure.
- Add main application class: `SubmersibleApplication`.
- Configure `pom.xml` with:
    - Spring Web
    - Validation (jakarta)
    - Spring Boot test dependencies (JUnit 5, MockMvc)

---

## 2. Grid Bounds Specification

**Commit:** `test: introduce GridTest for 3D bounds`

- Add `GridTest` to define expected behaviour of the 3D grid:
    - In-bounds vs out-of-bounds coordinates.
    - Basic grid dimension rules.
- No production `Grid` implementation yet → tests fail (RED).

---

## 3. Grid Implementation With Obstacles

**Commit:** `feat: implement Grid with bounds and obstacles`

- Implement `Grid` domain class with:
    - `width`, `height`, `depth`
    - `isWithinBounds(x, y, z)`
    - Obstacle support via `addObstacle(x, y, z)` and `isObstacle(...)`
- Use a `Set<String>` to store obstacles, so duplicates are naturally ignored.
- Make `GridTest` pass (GREEN).

---

## 4. Probe Movement Specification

**Commit:** `test: define probe movement and turning behaviour`

- Add `ProbeTest` describing:
    - Initialisation at a given `(x, y, z)` with `Direction`.
    - Turning left/right/up/down.
    - Moving forward/backward depending on direction.
    - Staying within grid bounds.
    - Preventing movement into obstacles.
    - Tracking visited coordinates.
- No `Probe` implementation yet → tests fail (RED).

---

## 5. Probe Domain Implementation

**Commit:** `feat: implement Probe with movement and visited tracking`

- Implement `Probe`:
    - Fields for current coordinates and `Direction`.
    - Methods: `moveForward`, `moveBackward`, `turnLeft`, `turnRight`, `turnUp`, `turnDown`.
    - Use `Grid` to block moves going out-of-bounds or onto obstacles.
    - Maintain `visitedCoordinates`, recording starting position and every valid move.
- Make all `ProbeTest` cases pass (GREEN).

---

## 6. Command Interpreter Specification

**Commit:** `test: define interpreter behaviour for valid and invalid commands`

- Add `CommandInterpreterTest` that specifies:
    - Handling of valid commands: `F`, `B`, `L`, `R`, `U`, `D`.
    - Handling of invalid commands (`null`, unknown symbols, multi-char tokens).
    - Tracking:
        - `totalCommands`
        - `executedCommands`
        - `blockedMoves`
        - `invalidCommands` list (index + command + reason).
- Interpreter not implemented yet → tests fail (RED).

---

## 7. Command Interpreter Implementation

**Commit:** `feat: implement CommandInterpreter and execution result model`

- Implement `CommandInterpreter`:
    - Loop over each command string and invoke the proper `Probe` method.
    - Distinguish between movement commands and turns.
    - When a move does not change the probe’s position, increment `blockedMoves`.
    - Record invalid commands with index and reason.
- Introduce internal execution result model and map it to DTO later.
- All `CommandInterpreterTest` tests now pass (GREEN).

---

## 8. Service Orchestration Specification

**Commit:** `test: describe ProbeRunService end-to-end behaviour`

- Add `ProbeRunServiceTest` specifying:
    - How `RunRequest` gets converted into `Grid`, `Probe` and command execution.
    - How final state is reflected in `RunResponse`.
    - That visited coordinates and execution statistics are exposed.
    - High-level textual summary requirements.
- Service not implemented yet → tests fail (RED).

---

## 9. ProbeRunService Implementation

**Commit:** `feat: implement ProbeRunService run() orchestration`

- Implement `ProbeRunService.run(RunRequest)`:
    - Build `Grid` from `GridDto` and add obstacles.
    - Validate start position is within bounds.
    - Create `Probe` with start coordinates and direction.
    - Use `CommandInterpreter` to execute commands.
    - Map final state, metrics, and visited path into `RunResponse`.
    - Build a human-readable summary (`summary` field).
- All `ProbeRunServiceTest` tests pass (GREEN).

---

## 10. Controller Happy-Path Specification

**Commit:** `test: add happy-path controller test for /api/probe/run`

- Add `ProbeControllerTest` to verify:
    - POST `/api/probe/run` with a valid payload returns HTTP 200.
    - Response contains non-null final state, execution, visited, and summary.
- Controller not implemented yet → tests fail (RED).

---

## 11. Controller Implementation

**Commit:** `feat: expose /api/probe/run endpoint`

- Implement `ProbeController`:
    - `POST /api/probe/run` that accepts `@Valid RunRequest`.
    - Delegates to `ProbeRunService`.
    - Wraps `RunResponse` in HTTP 200.
- Happy-path controller test passes (GREEN).

---

## 12. Error Handling Specification

**Commit:** `test: define API behaviour for invalid requests`

- Extend controller tests to expect structured JSON errors for:
    - Payload validation failures.
    - Domain validation failures (e.g. invalid start position).
- No global handler yet → tests fail or default error format used (RED).

---

## 13. Global Exception Handling Implementation

**Commit:** `feat: implement GlobalExceptionHandler and ErrorResponse`

- Implement `ErrorResponse` with:
    - `ErrorBody` (code, message, details, traceId).
    - `ErrorDetail` (field, issue).
- Implement `GlobalExceptionHandler` with mappings for:
    - `MethodArgumentNotValidException` → 400 + validation error payload.
    - `ConstraintViolationException` → 400 + constraint details.
    - `HttpMessageNotReadableException` → 400 + “Malformed JSON request”.
- Basic error-path tests now pass (GREEN).

---

## 14. Edge Case – Empty Command List

**Commit:** `test: document behaviour for empty command list`

- Add service-level test for `commands = []`:
    - Final state equals initial state.
    - All execution counters are zero.
- No production change required (test documents and locks in existing behaviour).

---

## 15. Edge Case – Start Position on Obstacle

**Commit:** `test: define behaviour when start position is an obstacle`

- Add failing test where:
    - Start coordinate coincides with an obstacle.
    - Expectation: request should fail fast.

**Commit:** `feat: fail fast when start position is an obstacle`

- Update `ProbeRunService`:
    - After verifying bounds, check `grid.isObstacle(startX, startY, startZ)`.
    - Throw `IllegalArgumentException("Start position cannot be an obstacle")` if true.
- Start-on-obstacle test passes (GREEN).

---

## 16. Edge Case – Duplicate Obstacles

**Commit:** `test: ensure duplicate obstacles in request are harmless`

- Add test running the same commands twice:
    - Once with a single obstacle.
    - Once with the same obstacle duplicated in the request.
- Assert final state, execution metrics, and visited coordinates are identical.
- Confirms `Set`-based obstacle storage behaves as expected.

---

## 17. Edge Case – Large Command List

**Commit:** `test: add sanity coverage for large command lists`

- Add a “large input” test:
    - Grid: large enough (e.g. 100 × 100 × 100).
    - Commands: 10,000 `F` moves.
    - Assert:
        - No exception thrown.
        - Final position remains within bounds.
- Provides lightweight performance sanity coverage for the kata.

---

## 18. TDD History Documentation

**Commit:** `docs: add TDD git history narrative`

- Add `GIT_HISTORY.md` describing the TDD evolution of the project.
- Summarise major commits, their intent, and how they relate to tests.

---

## 19. Controller Error-Path Tests

**Commit:** `test: add controller tests for malformed JSON and missing fields`

- Add tests to `ProbeControllerTest` for:
    - Malformed JSON → 400 + `"Malformed JSON request"`.
    - Missing required field (`grid = null`) → validation error payload.
- Initially expected 422 for missing field (RED).

**Commit:** `test: align mandatory-field validation test with 400 response`

- Adjust test to expect 400 (Bad Request), matching `MethodArgumentNotValidException`
  handling in `GlobalExceptionHandler`.
- Tests pass (GREEN).

---

## 20. Validation Utilities Fix

**Commit:** `feat: add validation overload and align GlobalExceptionHandler with ErrorResponse API`

- Add factory methods to `ErrorResponse`:
    - `validation(String msg)`
    - `validation(String msg, List<ErrorDetail> details)`
- Update `GlobalExceptionHandler` to use these factories.
- Ensure `IllegalArgumentException` is mapped to 422 with a consistent
  `"VALIDATION_ERROR"` error payload.

---

## 21. API Validation – Negative Coordinates

**Commit:** `test: add api validation test for negative coordinates`

- Add test posting a `RunRequest` with negative coordinates.
- Expect:
    - HTTP 400.
    - `VALIDATION_ERROR` with `grid` or `start` field issues from bean validation.
- Confirms `@Min(0)` constraints on coordinates are enforced at API level.

---

## 22. README Enhancements: Swagger & Performance Notes

**Commit:** `docs: document Swagger/OpenAPI endpoints in README`

- Update `README.md` with an “API Documentation (OpenAPI / Swagger)” section:
    - `http://localhost:8080/swagger-ui.html`
    - `http://localhost:8080/v3/api-docs`
- Explain how to access interactive docs and how to disable them in production.

**Commit:** `docs: add performance testing notes to README`

- Add section describing:
    - Large-input sanity test (`ProbeRunServiceTest.largeCommandList_isHandledWithinGridBounds`).
    - What is and isn’t covered (no full load or stress tests).
- Clarify that extended performance testing is out-of-scope for this kata.

---

## 23. CI Integration

**Commit:** `ci: add GitHub Actions workflow for Maven build and tests`

- Add `.github/workflows/maven-ci.yml`:
    - Trigger on push and PR to main/master.
    - Use JDK 21.
    - Run `mvn -B verify`.
- Ensures tests run automatically on each change.

---

## 24. OpenAPI Controller Documentation

**Commit:** `feat: enhance ProbeController with OpenAPI annotations`

- Annotate `ProbeController` with:
    - `@Tag` for controller grouping.
    - `@Operation` for endpoint summary and description.
    - `@ApiResponses` documenting 200/400/422 responses referencing `RunResponse`
      and `ErrorResponse`.
- No functional changes; improves Swagger documentation.

---

## 25. DTO OpenAPI Schema Annotations

**Commit:** `docs: add comprehensive OpenAPI schema annotations to all DTOs and error models`

- Add `@Schema` annotations to DTOs:
    - `RunRequest`, `RunResponse`
    - `GridDto`, `CoordinateDto`
    - `FinalStateDto`, `ExecutionDto`, `InvalidCommandDto`
- Add `@Schema` annotations to error classes:
    - `ErrorResponse`, `ErrorBody`, `ErrorDetail`
- Include field descriptions, examples, and high-level class descriptions.
- Restore `ErrorResponse.validation(...)` helpers for use in exception handling.

---

## 26. ExecutionDto Constructor Fix

**Commit:** `fix: restore ExecutionDto all-args constructor required by ProbeRunService`

- Reintroduce the all-args constructor in `ExecutionDto`:
    - Needed for `new ExecutionDto(totalCommands, executedCommands, blockedMoves, invalidCommands)`.
- Keep no-args constructor for deserialisation.
- Fixes compilation error introduced by DTO refactor.

---

## 27. Final DTO Pass & Swagger Polish

**Commit:** `docs: refine DTO schema documentation and align with current API contract`

- Final pass over all DTOs to ensure:
    - Types and field names match the latest implementation.
    - Swagger/OpenAPI schemas align with the actual JSON payload.
- Confirms the API is fully self-documented in Swagger UI.

---
