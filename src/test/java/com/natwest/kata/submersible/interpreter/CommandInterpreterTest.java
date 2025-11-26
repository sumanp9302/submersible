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

        // U and D are now ROTATION commands (NOT movement)
        ExecutionResult result = interpreter.execute(List.of("F","U","D","L","F"), probe);

        assertEquals(5, result.getTotalCommands());
        assertEquals(1, result.getBlockedMoves()); // forward into obstacle
        assertTrue(result.getInvalidCommands().isEmpty());

        // Execution:
        // F = blocked
        // U = turnUp
        // D = return to EAST (last horizontal)
        // L = NORTH
        // F = NORTH y+1 → (0,3,0)

        assertEquals(0, probe.getX());
        assertEquals(3, probe.getY());
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
        assertEquals(2, result.getInvalidCommands().size());

        assertEquals(1, result.getInvalidCommands().get(0).getIndex());
        assertEquals("X", result.getInvalidCommands().get(0).getCommand());
        assertEquals(3, result.getInvalidCommands().get(1).getIndex());
        assertEquals("Y", result.getInvalidCommands().get(1).getCommand());

        // Final state:
        // F → (0,1,0)
        // X → invalid
        // R → EAST
        // Y → invalid
        // B → backward EAST → x-1 → blocked
        assertEquals(0, probe.getX());
        assertEquals(1, probe.getY());
        assertEquals(Direction.EAST, probe.getDirection());

        assertEquals(1, result.getBlockedMoves());
    }

    @Test
    void shouldCountBlockedMovesForFandBOnly() {
        Grid grid = new Grid(2, 2, 2);
        Probe probe = new Probe(0, 0, 0, Direction.WEST, grid);

        CommandInterpreter interpreter = new CommandInterpreter();

        ExecutionResult result = interpreter.execute(List.of("B", "D", "L"), probe);

        // B when WEST → x-1 → -1 → blocked
        // D is rotation, not movement → no block
        // L is rotation → no block

        assertEquals(1, result.getBlockedMoves());
        assertEquals(Direction.SOUTH, probe.getDirection());
    }

    @Test
    void shouldRotateUpAndDownUsingUAndDCommands() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.NORTH, grid);

        CommandInterpreter interpreter = new CommandInterpreter();

        ExecutionResult result = interpreter.execute(List.of("U", "D"), probe);

        assertTrue(result.getInvalidCommands().isEmpty());

        // NORTH -> UP -> NORTH
        assertEquals(Direction.NORTH, probe.getDirection());
    }

    @Test
    void shouldRotateUpMoveForwardThenRotateDownAndMoveForward() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.NORTH, grid);

        CommandInterpreter interpreter = new CommandInterpreter();

        ExecutionResult result = interpreter.execute(List.of("U", "F", "D", "F"), probe);

        // Start: (5,5,5)
        // U → UP
        // F → (5,5,6)
        // D → NORTH
        // F → (5,6,6)

        assertEquals(5, probe.getX());
        assertEquals(6, probe.getY());
        assertEquals(6, probe.getZ());
        assertEquals(Direction.NORTH, probe.getDirection());
        assertEquals(0, result.getBlockedMoves());
    }

    @Test
    void shouldNotTreatUAndDAsInvalidCommands() {
        Grid grid = new Grid(10, 10, 10);
        Probe probe = new Probe(5, 5, 5, Direction.NORTH, grid);

        CommandInterpreter interpreter = new CommandInterpreter();

        ExecutionResult result = interpreter.execute(List.of("U", "X", "D"), probe);

        // Only X is invalid
        assertEquals(1, result.getInvalidCommands().size());
        assertEquals("X", result.getInvalidCommands().getFirst().getCommand());

        // NORTH -> UP -> NORTH
        assertEquals(Direction.NORTH, probe.getDirection());
    }

}
