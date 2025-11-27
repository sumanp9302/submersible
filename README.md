# Submersible Probe Control API
## A fully test-driven, cleanly architected REST API that simulates the movement of a remotely controlled 3D submersible probe navigating a grid with obstacles.
________________________________________
# Table of Contents

1.	Overview
2.	Features
3.	How to Run
4.	API Endpoint
5.	Example JSON Request / Response
6.	Swagger / OpenAPI Documentation
7.	Project Architecture
8.	TDD Overview
9.	Performance Testing Notes
10.	Folder Structure
11.	CI/CD
12.	References
________________________________________
1. Overview

   This API allows the surface control team to remotely navigate a 3D underwater probe using command strings:

   	•	Move forward/backward
   	•	Turn left/right
   	•	Move up/down (z-axis)
   	•	Avoid obstacles
   	•	Stay within grid boundaries
   	•	Produce a detailed movement summary

   The API includes extensive validation, error handling, and complete Swagger/OpenAPI documentation.

2. Features

   	•	✔ 3D grid navigation
   	•	✔ 6-direction probe movement (F, B, L, R, U, D)
   	•	✔ Obstacle detection & avoidance
   	•	✔ Grid boundary enforcement
   	•	✔ Tracks visited coordinates
   	•	✔ Detailed execution metrics
   	•	✔ Strong validation layer (400 & 422 handling)
   	•	✔ Standardized error payload structure
   	•	✔ Fully annotated OpenAPI schema
   	•	✔ 100% test-driven (TDD) development
   	•	✔ Includes performance sanity test
   	•	✔ CI workflow included

3. How to Run

   	Prerequisites
   		•	Java 21
   		•	Maven 3.9+

   	Run Application
   	mvn spring-boot:run
   	Run Tests
   	mvn test

   	The application runs at:
   	http://localhost:8080

4. API Endpoint

   	POST /api/probe/run
   	Executes a navigation sequence and returns final probe state, visited coordinates, and execution statistics.

5. Example JSON Request / Response

   	5.1 Example Request
   	{
   	  "grid": { "width": 5, "height": 5, "depth": 5 },
   	  "start": { "x": 0, "y": 0, "z": 0 },
   	  "direction": "NORTH",
   	  "commands": ["F", "R", "F", "U", "F"],
   	  "obstacles": [
   		{ "x": 1, "y": 1, "z": 0 }
   	  ]
   	}
   	5.2 Example Response
   	{
   	  "finalState": {
   		"x": 1,
   		"y": 1,
   		"z": 1,
   		"direction": "EAST"
   	  },
   	  "execution": {
   		"totalCommands": 5,
   		"executedCommands": 5,
   		"blockedMoves": 0,
   		"invalidCommands": []
   	  },
   	  "visited": [
   		"0,0,0",
   		"0,1,0",
   		"1,1,0",
   		"1,1,1",
   		"1,2,1"
   	  ],
   	  "grid": {
   		"width": 5,
   		"height": 5,
   		"depth": 5
   	  },
   	  "summary": "Probe completed 5 commands with 0 blocked and 0 invalid moves."
   	}

6. Swagger / OpenAPI Documentation

   	Once the app is running:
   	Swagger UI
   	http://localhost:8080/swagger-ui.html

   	OpenAPI (v3) JSON
   	http://localhost:8080/v3/api-docs

   All DTOs include clean OpenAPI @Schema annotations, and controller endpoints use @Operation + @ApiResponses.

7. Project Architecture

   	API Layer
   	│   ├── ProbeController
   	│   ├── Request/Response DTOs
   	│   ├── GlobalExceptionHandler
   	│   └── ErrorResponse model
   	│
   	Service Layer
   	│   └── ProbeRunService (core orchestration)
   	│
   	Interpreter Layer
   	│   └── CommandInterpreter (handles F/B/L/R/U/D)
   	│
   	Domain Layer
   	│   ├── Grid (bounds, obstacles)
   	│   └── Probe (movement, direction, visited path)
   	│
   	Infrastructure
   		└── springdoc-openapi (Swagger UI + OpenAPI)

   Design Principles Followed:

   		•	Separation of Concerns
   		•	Domain-driven
   		•	SOLID principles
   		•	Test-driven development

8. TDD Overview

   All behavior was developed using RED → GREEN → REFACTOR.

   What is covered by TDD?

   	•	Grid bounds
   	•	Obstacle behavior
   	•	All probe movements (F/B/L/R/U/D)
   	•	Turning logic
   	•	Command interpreter
   	•	Execution metrics
   	•	Start-on-obstacle scenario
   	•	Empty command list
   	•	Duplicate obstacles
   	•	Negative coordinate validation
   	•	API validation errors (400)
   	•	Domain-level errors (422)
   	•	Malformed JSON
   	•	Controller tests
   	•	Large-command performance sequence

   Full commit-by-commit narrative is available at:
   📄 GIT_HISTORY.md

9. Performance Testing Notes

   A lightweight but effective performance test exists:
   ProbeRunServiceTest.largeCommandList_isHandledWithinGridBounds

   This test ensures:

   	•	10,000 commands execute smoothly
   	•	Probe remains inside grid
   	•	No performance bottlenecks
   	•	No memory issues

10. Folder Structure

    submersible/
    │
    ├── src/
    │   ├── main/java/com/natwest/kata/submersible/...
    │   └── test/java/com/natwest/kata/submersible/...
    │
    ├── docs/
    │   └── architecture-tdd-summary.md
    │
    ├── GIT_HISTORY.md
    ├── README.md

11. CI/CD

        GitHub Actions pipeline triggers on push and pull requests:

    	•	Runs on JDK 21
    	•	Executes mvn verify (compile + tests)
    	•	Ensures no merge can occur if tests fail

12. References

    	•	Spring Boot 3
    	•	JUnit 5
    	•	MockMvc
    	•	Spring Validation (Jakarta)
    	•	springdoc-openapi
    	•	Test-Driven Development
    	•	SOLID & Clean Architecture
