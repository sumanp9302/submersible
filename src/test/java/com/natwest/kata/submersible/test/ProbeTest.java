package com.natwest.kata.submersible.test;

import com.natwest.kata.submersible.domain.Grid;
import com.natwest.kata.submersible.domain.Probe;
import com.natwest.kata.submersible.enums.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ProbeTest {

    @Test
    void shouldInitializeProbeWithGivenCoordinatesAndDirection() {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, Direction.NORTH, grid);
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(5, probe.getZ());
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    // --- TDD Stage 3: moveForward for ALL 6 directions ---
    @ParameterizedTest
    @CsvSource({
            // dir    expectedX, expectedY, expectedZ
            "NORTH, 2, 4, 5",
            "EAST,  3, 3, 5",
            "SOUTH, 2, 2, 5",
            "WEST,  1, 3, 5",
            "UP,    2, 3, 6",
            "DOWN,  2, 3, 4"
    })
    void shouldMoveForwardInAllDirections(Direction direction, int expectedX, int expectedY, int expectedZ) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, direction, grid);
        probe.moveForward();
        assertEquals(expectedX, probe.getX());
        assertEquals(expectedY, probe.getY());
        assertEquals(expectedZ, probe.getZ());
    }

    // --- TDD Stage 3: moveBackward for ALL 6 directions ---
    @ParameterizedTest
    @CsvSource({
            // dir    expectedX, expectedY, expectedZ
            "NORTH, 2, 2, 5",
            "EAST,  1, 3, 5",
            "SOUTH, 2, 4, 5",
            "WEST,  3, 3, 5",
            "UP,    2, 3, 4",
            "DOWN,  2, 3, 6"
    })
    void shouldMoveBackwardInAllDirections(Direction direction, int expectedX, int expectedY, int expectedZ) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, direction, grid);
        probe.moveBackward();
        assertEquals(expectedX, probe.getX());
        assertEquals(expectedY, probe.getY());
        assertEquals(expectedZ, probe.getZ());
    }

    @ParameterizedTest
    @CsvSource({"NORTH, WEST", "WEST, SOUTH", "SOUTH, EAST", "EAST, NORTH"})
    void shouldTurnLeft(Direction initial, Direction expected) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, initial, grid);
        probe.turnLeft();
        assertEquals(expected, probe.getDirection());
    }

    @ParameterizedTest
    @CsvSource({"NORTH, EAST", "EAST, SOUTH", "SOUTH, WEST", "WEST, NORTH"})
    void shouldTurnRight(Direction initial, Direction expected) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, initial, grid);
        probe.turnRight();
        assertEquals(expected, probe.getDirection());
    }

    @Test
    void shouldMoveUpAndDown() {
        Grid grid = new Grid(6, 6, 6);
        // Start at z = 4 so up -> 5, down -> 4 for a clean cycle
        Probe probe = new Probe(2, 3, 4, Direction.NORTH, grid);
        probe.moveUp();
        assertEquals(5, probe.getZ());
        probe.moveDown();
        assertEquals(4, probe.getZ());
    }

    @Test
    void shouldThroughExceptionWhenProbeInitializedOutsideGrid() {
        Grid grid = new Grid(5, 5, 5);
        assertThrows(IllegalArgumentException.class,
                () -> new Probe(6, 0, 0, Direction.NORTH, grid));
    }

    @Test
    void shouldNotMoveOutsideGridBounds() {
        Grid grid = new Grid(5, 5, 5);
        Probe probe = new Probe(0, 0, 0, Direction.NORTH, grid);
        probe.moveBackward();
        assertEquals(0, probe.getX());
        assertEquals(0, probe.getY());
    }

    @Test
    void shouldNotMoveIntoObstacles() {
        Grid grid = new Grid(6, 6, 6);
        grid.addObstacle(3, 3, 5);
        Probe probe = new Probe(2, 3, 5, Direction.EAST, grid);
        probe.moveForward();
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
    }

    @Test
    void shouldTrackVisitedCoordinates() {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(0, 0, 0, Direction.NORTH, grid);
        probe.moveForward(); // (0,1,0)
        probe.moveForward(); // (0,2,0)
        probe.turnRight();   // EAST
        probe.moveForward(); // (1,2,0)

        assertEquals(4, probe.getVisitedCoordinates().size());
        assertTrue(probe.getVisitedCoordinates().contains("(0,0,0)"));
        assertTrue(probe.getVisitedCoordinates().contains("(1,2,0)"));
    }

    @Test
    void shouldFaceUpAndDownUsingRotation() {
        Grid grid = new Grid(6, 6, 6);

        // Horizontal -> UP
        Probe probeUp = new Probe(2, 3, 2, Direction.NORTH, grid);
        probeUp.turnUp();
        assertEquals(Direction.UP, probeUp.getDirection());

        // Horizontal -> DOWN
        Probe probeDown = new Probe(2, 3, 2, Direction.EAST, grid);
        probeDown.turnDown();
        assertEquals(Direction.DOWN, probeDown.getDirection());
    }

    @Test
    void shouldNotTurnLeftOrRightWhenFacingVertical() {
        Grid grid = new Grid(6, 6, 6);

        // When facing UP, L/R should do nothing
        Probe probeUp = new Probe(2, 3, 2, Direction.NORTH, grid);
        probeUp.turnUp();
        probeUp.turnLeft();
        assertEquals(Direction.UP, probeUp.getDirection());
        probeUp.turnRight();
        assertEquals(Direction.UP, probeUp.getDirection());

        // When facing DOWN, L/R should do nothing
        Probe probeDown = new Probe(2, 3, 2, Direction.EAST, grid);
        probeDown.turnDown();
        probeDown.turnLeft();
        assertEquals(Direction.DOWN, probeDown.getDirection());
        probeDown.turnRight();
        assertEquals(Direction.DOWN, probeDown.getDirection());
    }

    @Test
    void shouldMoveForwardAndBackwardWhenFacingVertical() {
        Grid grid = new Grid(6, 6, 6);

        // Facing UP: forward -> z+1, backward -> z-1
        Probe upProbe = new Probe(2, 3, 2, Direction.NORTH, grid);
        upProbe.turnUp();
        upProbe.moveForward();
        assertEquals(3, upProbe.getZ());
        upProbe.moveBackward();
        assertEquals(2, upProbe.getZ());

        // Facing DOWN: forward -> z-1, backward -> z+1
        Probe downProbe = new Probe(2, 3, 2, Direction.NORTH, grid);
        downProbe.turnDown();
        downProbe.moveForward();
        assertEquals(1, downProbe.getZ());
        downProbe.moveBackward();
        assertEquals(2, downProbe.getZ());
    }

    @Test
    void shouldTurnUpFromAnyHorizontalDirection() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.NORTH, grid);
        probe.turnUp();
        assertEquals(Direction.UP, probe.getDirection());
    }

    @Test
    void shouldTurnDownFromAnyHorizontalDirection() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.EAST, grid);
        probe.turnDown();
        assertEquals(Direction.DOWN, probe.getDirection());
    }

    @Test
    void shouldReturnToPreviousHorizontalDirectionWhenComingDownFromUp() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.WEST, grid);
        probe.turnUp();   // now facing UP, lastHorizontal = WEST
        probe.turnDown(); // should restore lastHorizontal
        assertEquals(Direction.WEST, probe.getDirection());
    }

    @Test
    void shouldReturnToPreviousHorizontalDirectionWhenComingUpFromDown() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.SOUTH, grid);
        probe.turnDown(); // facing DOWN, lastHorizontal = SOUTH
        probe.turnUp();   // restore SOUTH
        assertEquals(Direction.SOUTH, probe.getDirection());
    }

    @Test
    void leftAndRightShouldDoNothingWhenFacingUpOrDown() {
        Grid grid = new Grid(10, 10, 10);

        Probe up = new Probe(5, 5, 5, Direction.NORTH, grid);
        up.turnUp();
        up.turnLeft();
        assertEquals(Direction.UP, up.getDirection());
        up.turnRight();
        assertEquals(Direction.UP, up.getDirection());

        Probe down = new Probe(5, 5, 5, Direction.EAST, grid);
        down.turnDown();
        down.turnLeft();
        assertEquals(Direction.DOWN, down.getDirection());
        down.turnRight();
        assertEquals(Direction.DOWN, down.getDirection());
    }
}
