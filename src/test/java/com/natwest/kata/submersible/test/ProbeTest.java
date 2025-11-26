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

    @ParameterizedTest
    @CsvSource({
            "NORTH, 2, 4, 5",
            "EAST, 3, 3, 5",
            "SOUTH, 2, 2, 5",
            "WEST, 1, 3, 5"
    })
    void shouldMoveForward(Direction direction, int expectedX, int expectedY, int expectedZ) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, direction, grid);
        probe.moveForward();
        assertEquals(expectedX, probe.getX());
        assertEquals(expectedY, probe.getY());
        assertEquals(expectedZ, probe.getZ());
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, 2, 2, 5",
            "EAST, 1, 3, 5",
            "SOUTH, 2, 4, 5",
            "WEST, 3, 3, 5"
    })
    void shouldMoveBackward(Direction direction, int expectedX, int expectedY, int expectedZ) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, direction, grid);
        probe.moveBackward();
        assertEquals(expectedX, probe.getX());
        assertEquals(expectedY, probe.getY());
        assertEquals(expectedZ, probe.getZ());
    }


    @ParameterizedTest
    @CsvSource({
            "NORTH, WEST",
            "WEST, SOUTH",
            "SOUTH, EAST",
            "EAST, NORTH"
    })
    void shouldTurnLeft(Direction initial, Direction expected) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, initial, grid);
        probe.turnLeft();
        assertEquals(expected, probe.getDirection());
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, EAST",
            "EAST, SOUTH",
            "SOUTH, WEST",
            "WEST, NORTH"
    })
    void shouldTurnRight(Direction initial, Direction expected) {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, initial, grid);
        probe.turnRight();
        assertEquals(expected, probe.getDirection());
    }

    @Test
    void shouldMoveUpAndDown() {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 5, Direction.NORTH, grid);
        probe.moveUp();
        assertEquals(5, probe.getZ());
        probe.moveDown();
        assertEquals(4, probe.getZ());
    }

    @Test
    void shouldThroughExceptionWhenProbeInitializedOutsideGrid() {
        Grid grid = new Grid(5, 5, 5);
        assertThrows(IllegalArgumentException.class, () -> new Probe(6, 0, 0, Direction.NORTH, grid));
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
    void shouldNotMoveIntoObstacles(){
        Grid grid = new Grid(6,6,6);
        grid.addObstacle(3,3,5);
        Probe probe = new Probe(2,3,5, Direction.EAST, grid);
        probe.moveForward();
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
    }

    @Test
    void shouldTrackVisitedCoordinates(){
        Grid grid = new Grid(6,6,6);
        Probe probe = new Probe(0,0,0, Direction.NORTH, grid);
        probe.moveForward();
        probe.moveForward();
        probe.turnRight();
        probe.moveForward();

        assertEquals(4, probe.getVisitedCoordinates().size());

        assertTrue(probe.getVisitedCoordinates().contains("(0,0,0)"));
        assertTrue(probe.getVisitedCoordinates().contains("(1,2,0)"));

    }

    @Test
    void shouldFaceUpAndDownUsingRotation() {
        Grid grid = new Grid(6, 6, 6);
        Probe probe = new Probe(2, 3, 2, Direction.NORTH, grid);

        // Rotate to UP
        probe.turnUp();
        assertEquals(Direction.UP, probe.getDirection());

        // Rotate to DOWN
        probe.turnDown();
        assertEquals(Direction.DOWN, probe.getDirection());
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
        Probe probe = new Probe(2, 3, 2, Direction.NORTH, grid);

        // Facing UP: forward -> z+1, backward -> z-1
        probe.turnUp();
        probe.moveForward();
        assertEquals(3, probe.getZ());
        probe.moveBackward();
        assertEquals(2, probe.getZ());

        // Facing DOWN: forward -> z-1, backward -> z+1
        probe.turnDown();
        probe.moveForward();
        assertEquals(1, probe.getZ());
        probe.moveBackward();
        assertEquals(2, probe.getZ());
    }


}
