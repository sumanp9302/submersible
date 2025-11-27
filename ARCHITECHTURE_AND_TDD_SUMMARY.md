# Architecture & TDD Summary

This project demonstrates a clean, modular, and fully test-driven implementation of a 3D submersible probe control system. The solution follows clear architectural layering, adheres to SOLID principles, and is validated with comprehensive unit and integration tests.

---
## 🧱 Architecture Overview

### 1. API Layer
- **Controller** exposes REST endpoints (`/api/probe/run`).
- **DTOs** encapsulate request/response structures.
- **Swagger/OpenAPI** provides self-documenting contract.
- **GlobalExceptionHandler** ensures consistent structured error responses.

### 2. Service Layer
- **ProbeRunService** orchestrates:
    - Grid creation
    - Start coordinate validation
    - Obstacle injection
    - Interaction with domain models
    - Summary generation for response

### 3. Domain Layer
- Models for `Grid`, `Probe`, and `Direction`.
- Encapsulates movement, bounds checks, obstacle avoidance, and visited path tracking.
- Pure logic, free from framework dependencies.

### 4. Interpreter Layer
- Command interpreter that translates `F`, `B`, `L`, `R`, `U`, `D` into probe movements.
- Tracks execution metrics:
    - total commands,
    - executed commands,
    - blocked moves,
    - invalid commands.

### 5. Error Layer
- Standardized `ErrorResponse` format:
    - `code`
    - `message`
    - `details`
    - `traceId`

---

## 🧪 Test-Driven Development (TDD)

The project follows a strict TDD cycle:

### RED → GREEN → REFACTOR

### What Was Built Through TDD?

#### ✔ Grid behaviour
- Bounds
- Obstacles
- Duplicate obstacle handling

#### ✔ Probe domain logic
- Movement in 6 directions
- Turning behaviour
- Obstacle & bounds handling
- Visited-path tracking

#### ✔ Command interpreter
- Parsing
- Execution metrics
- Invalid command detection

#### ✔ Full service orchestration
- From RunRequest → RunResponse
- Summary creation
- End-to-end correctness

#### ✔ API layer
- Happy path (200 OK)
- Validation failures (400)
- Domain failures (422)
- Malformed JSON (400)

#### ✔ Edge cases
- Empty command list
- Start on an obstacle (fail fast)
- Large command lists (10,000 commands)
- Negative coordinates

#### ✔ Performance Sanity Test
- Ensures high-volume commands don’t degrade basic runtime behaviour.

---

## 📄 Documentation Enhancements

### Swagger/OpenAPI
- Full schema coverage across every DTO.
- Controller documented with:
    - `@Tag`
    - `@Operation`
    - `@ApiResponses`
- Error model documented for API visibility.

### README includes:
- Setup instructions
- Sample input/output
- Swagger usage
- Performance testing notes
- CI info
- TDD history

---

## ⚙️ CI/CD
- GitHub Actions pipeline:
    - JDK 21
    - `mvn verify` on each push/pr
    - Ensures tests always pass before merging

---

## 🎯 Result
A clean, well-architected, production-style codebase demonstrating strong command over:

- Domain modeling
- API design
- Clean code & SOLID principles
- TDD discipline
- Validation & exception handling
- OpenAPI/Swagger documentation
- CI-driven reliability

Suitable for senior-level interviews and real team environments.
