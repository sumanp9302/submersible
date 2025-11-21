package com.natwest.kata.submersible.test;


import com.natwest.kata.submersible.entities.Probe;
import com.natwest.kata.submersible.enums.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class ProbeTest {

    @Test
    void shouldInitializeProbeWithGivenCoordinatesAndDirection() {
        Probe probe = new Probe(2, 3, 5, Direction.NORTH);
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(5, probe.getZ());
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    /*-------------Upward Movement-----------------*/
    @Test
    void shouldMoveUpIn3DGrid() {
        Probe probe = new Probe(2, 3, 5, Direction.NORTH);
        probe.moveUp();
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(6, probe.getZ());
    }

    @Test
    void shouldMoveDownIn3DGrid() {
        Probe probe = new Probe(2, 3, 5, Direction.NORTH);
        probe.moveDown();
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(4, probe.getZ());
    }

    @ParameterizedTest
    @CsvSource({
            "NORTH, 2, 4, 5",
            "EAST, 3, 3, 5",
            "SOUTH, 2, 2, 5",
            "WEST, 1, 3, 5",
    })
    void shouldMoveForward(Direction direction, int expectedX, int expectedY, int expectedZ) {
        Probe probe = new Probe(2, 3, 5, direction);
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
        Probe probe = new Probe(2, 3, 5, direction);
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
        Probe probe = new Probe(2, 3, 5, initial);
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
        Probe probe = new Probe(2, 3, 5, initial);
        probe.turnRight();
        assertEquals(expected, probe.getDirection());
    }

}
