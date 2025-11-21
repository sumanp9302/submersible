1.  Project Setup
    •	Created a Spring Boot project.
    •	Initialized Git and committed the base setup.
    
2.  TDD Approach
    •	Wrote failing tests first, then implemented minimal code to pass them.
    •	Maintained small, meaningful commits for each step.
    
3.  Implemented Core Domain
    •	Probe class with x, y, z coordinates and Direction enum.
    •	Supported directions: NORTH, SOUTH, EAST, WEST.
    
4.  Features Implemented
    •	Initialization: Verified probe starts at correct coordinates and direction.
    •	Movement: 
        •	moveForward() → Moves in the direction (x/y changes).
        •	moveBackward() → Reverse movement.
    •	Turning: 
        •	turnLeft() → Rotates counter-clockwise.
    •	Unit Tests: 
        •	Initialization test.
        •	Movement tests for forward/backward.
        •	Turning tests for turnLeft() (all directions).
        
5.  Git History
    •	Each step committed separately: 
    •	Failing test → Commit.
    •	Implementation → Commit.
    •	Additional tests → Commit.
