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

    @Test
    void shouldMoveForwardIn3DGridBasedOnDirection()
    {
        Probe probe = new Probe(2,3,5, Direction.NORTH);
        probe.moveForward();
        assertEquals(2, probe.getX());
        assertEquals(4, probe.getY());
        assertEquals(5, probe.getZ());
    }

    @Test
    void shouldMoveBackwardIn3DGridBasedOnDirection()
    {
        Probe probe = new Probe(2,3,5, Direction.NORTH);
        probe.moveBackward();
        assertEquals(2, probe.getX());
        assertEquals(3, probe.getY());
        assertEquals(5, probe.getZ());
    }
}
