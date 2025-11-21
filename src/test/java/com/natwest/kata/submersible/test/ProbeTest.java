package com.natwest.kata.submersible.test;


import com.natwest.kata.submersible.entities.Probe;
import com.natwest.kata.submersible.enums.Direction;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProbeTest {

    @Test
    void shouldInitializeProbeWithGivenCoordinatesAndDirection()
    {
        Probe probe = new Probe(2, 3, 5, Direction.NORTH);
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(5, probe.getZ());
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    //Forward Movement
    @Test
    void shouldMoveForwardIn3DGridBasedOnDirection()
    {
        Probe probe = new Probe(2,3,5, Direction.NORTH);
        probe.moveForward();
        assertEquals(2, probe.getX());
        assertEquals(4, probe.getY());
        assertEquals(5, probe.getZ());
    }

    //Backward Movement
    @Test
    void shouldMoveBackwardIn3DGridBasedOnDirection()
    {
        Probe probe = new Probe(2,3,5, Direction.NORTH);
        probe.moveBackward();
        assertEquals(2, probe.getX());
        assertEquals(2, probe.getY());
        assertEquals(5, probe.getZ());
    }

    /*----------------Left Turning------------*/
    @Test
    void shouldTurnLeftFromNorthToWest()
    {
        Probe probe = new Probe(2,3,5,Direction.NORTH);
        probe.turnLeft();
        assertEquals(Direction.WEST, probe.getDirection());
    }

    @Test
    void shouldTurnLeftFromWestToSouth()
    {
        Probe probe = new Probe(2,3,5,Direction.WEST);
        probe.turnLeft();
        assertEquals(Direction.SOUTH, probe.getDirection());
    }

    @Test
    void shouldTurnLeftFromSouthToEast()
    {
        Probe probe = new Probe(2,3,5,Direction.SOUTH);
        probe.turnLeft();
        assertEquals(Direction.EAST, probe.getDirection());
    }

    @Test
    void shouldTurnLeftFromEastToNorth()
    {
        Probe probe = new Probe(2,3,5,Direction.EAST);
        probe.turnLeft();
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    /*----------------Right Turning------------*/
    @Test
    void shouldTurnRightFromNorthToEast()
    {
        Probe probe = new Probe(2,3,5,Direction.NORTH);
        probe.turnRight();
        assertEquals(Direction.EAST, probe.getDirection());
    }

    @Test
    void shouldTurnRightFromEastToSouth() {
        Probe probe = new Probe(2, 3, 5, Direction.EAST);
        probe.turnRight();
        assertEquals(Direction.SOUTH, probe.getDirection());
    }

    @Test
    void shouldTurnRightFromSouthToWest() {
        Probe probe = new Probe(2, 3, 5, Direction.SOUTH);
        probe.turnRight();
        assertEquals(Direction.WEST, probe.getDirection());
    }

    @Test
    void shouldTurnRightFromWestToNorth() {
        Probe probe = new Probe(2, 3, 5, Direction.WEST);
        probe.turnRight();
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    /*-------------Upward Movement-----------------*/
    void shouldMoveUpIn3DGrid(){
        Probe probe = new Probe(2,3,5, Direction.NORTH);
        probe.moveUp();
        assertEquals(2,probe.getX());
        assertEquals(3,probe.getY());
        assertEquals(6,probe.getZ());
    }

}
