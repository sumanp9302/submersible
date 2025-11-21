✅ What you’ve already done
[1] Basic model + movement (3D)
•	Created Direction enum: NORTH, SOUTH, EAST, WEST.
•	Created Probe with fields: x, y, z, direction.
•	Implemented:
o	moveForward()
o	moveBackward()
o	turnLeft()
o	turnRight()
o	moveUp()
o	moveDown()
•	Wrote tests:
o	shouldInitializeProbeWithGivenCoordinatesAndDirection
o	Parameterized tests for:
	shouldMoveForward(...)
	shouldMoveBackward(...)
o	Parameterized tests for:
	shouldTurnLeft(...)
	shouldTurnRight(...)
o	shouldMoveUpAndDown
👉 You now have a working, unit-tested 3D probe that moves in free space.
________________________________________

⏭️ Your next immediate step (when you resume)
[2] Introduce a Grid and enforce boundaries (“Stay on the grid”)
Right now, your probe can wander off to negative coordinates or very large numbers.
Goal of this step
•	Create a Grid concept.
•	Make sure the probe never leaves the grid.
•	If a move would go out of bounds, that move is ignored, and the probe stays where it is.
What to do (TDD way)
1.	Write tests first (new test class, no implementation yet):
o	GridTest – to check:
	Valid positions are inside bounds.
	Invalid positions are outside bounds.
o	ProbeGridMovementTest – to check:
	Probe placed at edge cannot move further in that direction.
	Position remains unchanged when a move would go out of bounds.
2.	Then implement:
o	A Grid class (e.g. with width, height).
o	Update Probe constructor to take a Grid.
o	Update moveForward / moveBackward to:
	Calculate candidate position.
	Check with grid if it’s valid.
	Only update coordinates if inside the grid.
👉 When you come back later, start exactly from: "Create tests for Grid and boundary behavior".
________________________________________
🔜 Roadmap of later steps (in recommended order)
After step [2], you can follow this sequence:
________________________________________


[3] Add obstacles in the grid (“Avoid obstacles”)
Goal
The probe should not move into coordinates that contain obstacles.
TDD steps
•	Add tests:
o	When next position is an obstacle → probe stays in current position.
o	Movement in other directions still works.
•	Implement:
o	Grid holds a set of obstacle positions.
o	Probe checks grid.isObstacle(candidatePosition) before moving, and ignores the move if true.
________________________________________
[4] Track visited coordinates (“Print summary of coordinates visited”)
Goal
Keep a history of all coordinates visited, including the starting point.
TDD steps
•	Tests:
o	After a sequence of valid moves, visited list contains start + each successfully moved position.
o	Moves blocked by boundary or obstacle should not add a new entry.
•	Implementation:
o	Add List<Position> visited (or similar) inside Probe.
o	Add start position in the constructor.
o	On every successful move, append the new position.
(Printing/summarizing can be done later, at API layer or as a simple method like getVisited().)
________________________________________
[5] Execute command sequences (F/B/L/R)
Goal
Instead of calling moveForward() etc. one by one, send a string like "FFRBL" and interpret it.
TDD steps
•	Tests:
o	Define a few sequences and expected final position + direction.
o	Include a mix of F, B, L, R (and possibly edge cases like empty string).
•	Implementation:
o	Add execute(String commands) to Probe.
o	Loop over characters and call the appropriate method.
o	For invalid characters: either throw IllegalArgumentException or skip (decide and test for it).
________________________________________
[6] Refactor direction logic into Direction enum (optional but clean)
Goal
Make the code cleaner by moving “turn left/right” logic to Direction.
TDD steps
•	Add tests in a DirectionTest class:
o	Turning left from each direction gives correct result.
o	Turning right from each direction gives correct result.
•	Implementation:
o	Add turnLeft() and turnRight() methods in Direction.
o	Update Probe.turnLeft() and Probe.turnRight() to use direction = direction.turnLeft() etc.
________________________________________
[7] Decide on 2D vs 3D model alignment with problem statement
Your kata statement mentions only x/y grid, no z.
You currently have z and moveUp/moveDown.
You have two options:
•	Option A – Stick with 3D: keep z and say your solution is an extended version.
•	Option B – Move to 2D for the “official” kata:
o	Ignore z in the main kata.
o	Keep 3D version in a separate branch or package as a bonus.
Whichever you decide, note it in a README or comment so future you aren’t confused.
________________________________________



[8] REST API – wrap the domain
After the core domain is solid and well tested, start on REST:
Goal
Expose an endpoint that:
•	Accepts grid size, obstacles, start position, start direction, and command string
•	Returns final position and path (visited coordinates)
TDD steps
•	Define request/response DTOs.
•	Write Spring MVC tests (using @WebMvcTest or similar) to:
o	Call the endpoint with JSON.
o	Assert on response body and status.
•	Implement:
o	ProbeService to map DTO → domain objects → response DTO.
o	ProbeController with a POST /probe/commands endpoint.
________________________________________
[9] Edge cases & polish
Once everything works:
•	Input validation tests:
o	Invalid grid size (0, negative).
o	Start position outside grid.
o	Start position on obstacle.
o	Empty / null command string.
•	Behavior tests:
o	Only turns (no movement).
o	Commands that hit boundaries and obstacles in sequence.
•	Logging / error messages:
o	Return meaningful error messages from the API.
________________________________________
[10] Git history best practices
When you work through this list next time, try to structure commits like:
•	feat: add grid boundaries to probe movement
•	test: add obstacle avoidance tests
•	feat: implement obstacles in grid
•	refactor: move direction turning logic to enum
•	feat: add execute command sequence to probe
•	feat: expose probe control REST endpoint
Small, focused commits will make your learning journey very clear.

