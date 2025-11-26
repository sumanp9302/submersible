package com.natwest.kata.submersible.interpreter;

import com.natwest.kata.submersible.domain.Grid;
import com.natwest.kata.submersible.domain.Probe;
import com.natwest.kata.submersible.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandInterpreterTest {

    @Test
    void shouldExecuteValidCommandsAndReturnBlockedCount(){
        Grid grid = new Grid(5,5,5);
        grid.addObstacle(1,2,0);
        Probe probe = new Probe(0,2,0, Direction.EAST, grid);

        CommandInterpreter interpreter = new CommandInterpreter();
        ExecutionResult result = interpreter.execute(List.of("F","U","D","L","B"), probe);

        assertEquals(5, result.getTotalCommands());
        assertEquals(1, result.getBlockedMoves());
        assertTrue(result.getInvalidCommands().isEmpty());

        // F blocked; U (+z) then D (-z); L → NORTH; B (backward facing NORTH) → y-1
        assertEquals(0, probe.getX());
        assertEquals(1, probe.getY());
        assertEquals(0, probe.getZ());
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    @Test
    void shouldRecordInvalidCommandsAndContinue() {
        Grid grid = new Grid(5, 5, 5);
        Probe probe = new Probe(0, 0, 0, Direction.NORTH, grid);

        CommandInterpreter interpreter = new CommandInterpreter();
        ExecutionResult result = interpreter.execute(List.of("F", "X", "R", "Y", "B"), probe);

        assertEquals(5, result.getTotalCommands());
        assertEquals(0, result.getBlockedMoves());
        assertEquals(2, result.getInvalidCommands().size());
        assertEquals(1, result.getInvalidCommands().get(0).getIndex());
        assertEquals("X", result.getInvalidCommands().get(0).getCommand());
        assertEquals(3, result.getInvalidCommands().get(1).getIndex());
        assertEquals("Y", result.getInvalidCommands().get(1).getCommand());

        // F: y+1; R: EAST; B while EAST means x-1? No—Backward opposite of EAST → WEST → x-1 blocked at 0?
        // Careful: your Probe's moveBackward relative to EAST does x+1 (correct). So final x=1.
        assertEquals(1, probe.getX());
        assertEquals(1, probe.getY());
        assertEquals(Direction.EAST, probe.getDirection());
    }

    @Test
    void shouldCountBlockedMovesAtGridBoundaries() {
        Grid grid = new Grid(2, 2, 2);
        Probe probe = new Probe(0, 0, 0, Direction.WEST, grid);

        CommandInterpreter interpreter = new CommandInterpreter();
        ExecutionResult result = interpreter.execute(List.of("B", "D", "L"), probe);
        // B with WEST → x+1 (to 1) allowed; D → z-1 blocked at 0; L → turn SOUTH

        assertEquals(3, result.getTotalCommands());
        assertEquals(1, result.getBlockedMoves());

        assertEquals(1, probe.getX());
        assertEquals(0, probe.getY());
        assertEquals(0, probe.getZ());
        assertEquals(Direction.SOUTH, probe.getDirection());
    }
}
