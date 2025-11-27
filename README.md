# 🚢 Submersible Probe Navigation API

A fully test-driven, professionally architected **3D submersible probe navigation system** built with **Java 21 + Spring Boot 3**.

The system simulates the navigation of a remote probe exploring a 3D ocean floor grid, with:

- 3D movement (x, y, z)
- Obstacle avoidance
- Command interpreter
- Full execution metrics
- Swagger/OpenAPI documentation
- Clean architecture & SOLID principles
- Complete TDD commit history
- Full domain-driven modeling

---

# 📑 Table of Contents

1. Overview
2. Features
3. Tech Stack
4. Architecture Overview
5. Swagger / API Docs
6. API Usage Example
7. Error Model
8. TDD Process
9. Performance Testing Notes
10. Run Locally
11. Project Structure
12. Diagrams Reference
13. CI/CD Pipeline

---

# 🧭 Overview

This REST API remotely controls a **submersible probe** operating inside a **3D grid (x/y/z)**.

The API accepts:

- Grid dimensions
- Starting coordinate
- Starting direction
- Movement commands
- Obstacles

The API returns:

- Final coordinates
- Final direction
- Path visited
- Execution breakdown (executed, blocked, invalid)

The entire implementation follows **Test-Driven Development** (TDD).

---

# ✨ Features

### Navigation
- Move Forward (F)
- Move Backward (B)
- Turn Left (L)
- Turn Right (R)
- Move Up (U)
- Move Down (D)

### Safety Rules
- Cannot leave the grid
- Cannot move into obstacles
- Invalid commands are tracked
- Blocked moves are recorded

### Execution Summary
- Final position & direction
- Full visited coordinate history
- Counts of executed/blocked/invalid commands
- List of invalid commands

### Documentation
- Swagger UI
- OpenAPI JSON
- DTO schemas with examples
- Unified error model

---

# 🧱 Tech Stack

- **Java 21**
- **Spring Boot 3.5.x**
- **Spring Web**
- **springdoc-openapi**
- **JUnit 5 / Mockito**
- **Maven**

---

# 🏗 Architecture Overview

The application uses a clean, modular architecture:

### 1. API Layer
- REST controller
- DTOs
- Validation
- Global exception handler
- Swagger annotations

### 2. Service Layer
- Orchestrates grid, probe, interpreter
- Aggregates final result DTO

### 3. Domain Layer *(pure Java — no Spring dependencies)*
- `Grid`
- `Probe`
- `Direction`
- Movement rules
- Rotation rules
- Boundary enforcement

### 4. Interpreter Layer
- Parses commands
- Executes safe movements
- Tracks invalid + blocked commands

### 5. Error Layer
- Unified `ErrorResponse`
- Mapped domain exceptions
- Validation error handling

Full architecture explanation + diagrams are in:

📄 **ARCHITECTURE_AND_TDD_SUMMARY.md**

---

# 📘 Swagger API Documentation

Swagger UI:  
http://localhost:8080/swagger-ui.html

OpenAPI JSON:  
http://localhost:8080/v3/api-docs

Swagger includes:
- Endpoint documentation
- Request/response models
- Error model
- Field descriptions
- Example payloads

---

# 📡 API Usage Example

POST `/api/probe/run`

### Request
{
"grid": { "width": 5, "height": 5, "depth": 5 },
"start": { "x": 0, "y": 0, "z": 0 },
"direction": "NORTH",
"commands": ["F", "R", "F", "U", "F"],
"obstacles": [{ "x": 2, "y": 0, "z": 0 }]
}

### Response (200)
{
"finalState": {
"x": 1,
"y": 1,
"z": 1,
"direction": "UP"
},
"visited": [
{"x":0,"y":0,"z":0},
{"x":0,"y":1,"z":0},
{"x":1,"y":1,"z":0},
{"x":1,"y":1,"z":1}
],
"execution": {
"executed": 4,
"blocked": 0,
"invalid": []
}
}

---

# ❌ Error Model

Errors use a unified structure:

{
"error": {
"code": "VALIDATION_ERROR",
"message": "Payload validation failed",
"details": [
{ "field": "grid", "issue": "must not be null" }
],
"traceId": null
}
}

### HTTP Errors
400 → Input validation failure, malformed JSON  
422 → Domain violations (start on obstacle, invalid grid)

---

# 🧪 TDD Process

This project strictly follows **Red → Green → Refactor**.

### Fully TDD-Driven Areas:
- Grid boundary rules
- Obstacle detection
- Six-direction movement
- Rotation logic
- Probe movement unit tests
- Command interpreter flow
- Invalid + blocked command logic
- Service-layer orchestration
- Controller integration tests
- DTO validation tests
- Error handler tests
- Performance sanity test (10k commands)

Full TDD Git history is documented in:

📄 **GIT_HISTORY.md**

---

# ⚡ Performance Testing Notes

A lightweight yet effective performance assurance test is included:

### ✔ Large Input Sanity Test
`ProbeRunServiceTest.largeCommandList_isHandledWithinGridBounds`

Validates:
- Handling of 10,000 commands
- Grid safety rules remain intact
- No performance degradation

### ❌ Not Included (Out of Scope)
- Load testing
- Stress testing
- Memory profiling
- Throughput/latency benchmarking

---

# ▶ Run Locally

Clone:  
git clone <your-repo-url>  
cd submersible

Build:  
mvn clean install

Run:  
mvn spring-boot:run

Test:  
mvn test

---

# 📂 Project Structure

src/  
├── main/java/com/natwest/kata/submersible  
│     ├── api  
│     │     ├── controller  
│     │     ├── dto  
│     │     ├── error  
│     ├── domain  
│     ├── interpreter  
│     ├── service  
│     └── SubmersibleApplication.java  
│  
├── test/java/com/natwest/kata/submersible  
│     ├── api/controller  
│     ├── domain  
│     ├── interpreter  
│     ├── service  
│  
├── resources  
│     └── application.yml

---

# 🧬 Diagrams Reference

All diagrams are available in **ARCHITECTURE_AND_TDD_SUMMARY.md**:

- Sequence diagram
- Component diagram
- Class diagram
- Deployment diagram
- State machine diagram
- Flow diagrams
- Domain behavior diagrams
- Full architecture write-up

---

# 🔄 CI/CD Pipeline

CI pipeline located at `.github/workflows/build.yml`:

- Runs on every push + PR
- Uses JDK 21
- Runs build & unit tests
- Ensures code quality and TDD consistency
- Prevents regressions

---