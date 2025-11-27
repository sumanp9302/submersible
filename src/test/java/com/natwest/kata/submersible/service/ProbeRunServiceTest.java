package com.natwest.kata.submersible.service;

import com.natwest.kata.submersible.api.dto.CoordinateDto;
import com.natwest.kata.submersible.api.dto.ExecutionDto;
import com.natwest.kata.submersible.api.dto.FinalStateDto;
import com.natwest.kata.submersible.api.dto.GridDto;
import com.natwest.kata.submersible.api.dto.RunRequest;
import com.natwest.kata.submersible.api.dto.RunResponse;
import com.natwest.kata.submersible.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProbeRunServiceTest {

    private final ProbeRunService service = new ProbeRunService();

    private RunRequest baseRequest() {
        RunRequest req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setStart(new CoordinateDto(1, 2, 3));
        req.setDirection(Direction.EAST);
        req.setCommands(Collections.singletonList("F"));
        req.setObstacles(Collections.emptyList());
        return req;
    }

    @Test
    void emptyCommands_keepsInitialState_andZeroExecutionTotals() {
        // given
        RunRequest req = baseRequest();
        req.setCommands(Collections.emptyList()); // empty commands

        // when
        RunResponse resp = service.run(req);

        // then: final state equals initial state
        FinalStateDto finalState = resp.getFinalState();
        assertNotNull(finalState);
        assertEquals(req.getStart().getX(), finalState.getX());
        assertEquals(req.getStart().getY(), finalState.getY());
        assertEquals(req.getStart().getZ(), finalState.getZ());
        assertEquals(req.getDirection(), finalState.getDirection());

        // and: execution totals are all zero
        ExecutionDto execution = resp.getExecution();
        assertNotNull(execution);
        assertEquals(0, execution.getTotalCommands());
        assertEquals(0, execution.getExecutedCommands());
        assertEquals(0, execution.getBlockedMoves());
        assertTrue(execution.getInvalidCommands().isEmpty());
    }

    @Test
    void startOnObstacle_throwsIllegalArgumentException() {
        // given
        RunRequest req = baseRequest();
        CoordinateDto start = req.getStart();

        req.setObstacles(Collections.singletonList(
                new CoordinateDto(start.getX(), start.getY(), start.getZ())
        ));
        req.setCommands(Collections.emptyList());

        // when / then
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.run(req)
        );

        assertTrue(
                ex.getMessage().toLowerCase().contains("obstacle"),
                "Exception message should mention obstacle"
        );
    }

    @Test
    void duplicateObstacles_doNotChangeResult() {
        // given
        GridDto grid = new GridDto(5, 5, 5);
        CoordinateDto start = new CoordinateDto(0, 0, 0);
        Direction direction = Direction.EAST;
        List<String> commands = List.of("F", "F", "F");

        CoordinateDto obstacle = new CoordinateDto(1, 0, 0);

        RunRequest single = new RunRequest();
        single.setGrid(grid);
        single.setStart(start);
        single.setDirection(direction);
        single.setCommands(commands);
        single.setObstacles(Collections.singletonList(obstacle));

        RunRequest duplicate = new RunRequest();
        duplicate.setGrid(grid);
        duplicate.setStart(start);
        duplicate.setDirection(direction);
        duplicate.setCommands(commands);
        duplicate.setObstacles(List.of(obstacle, obstacle)); // duplicate entries

        // when
        RunResponse respSingle = service.run(single);
        RunResponse respDup = service.run(duplicate);

        // then: same final state
        FinalStateDto fsSingle = respSingle.getFinalState();
        FinalStateDto fsDup = respDup.getFinalState();

        assertEquals(fsSingle.getX(), fsDup.getX());
        assertEquals(fsSingle.getY(), fsDup.getY());
        assertEquals(fsSingle.getZ(), fsDup.getZ());
        assertEquals(fsSingle.getDirection(), fsDup.getDirection());

        // and: same execution counters
        ExecutionDto exSingle = respSingle.getExecution();
        ExecutionDto exDup = respDup.getExecution();

        assertEquals(exSingle.getTotalCommands(), exDup.getTotalCommands());
        assertEquals(exSingle.getExecutedCommands(), exDup.getExecutedCommands());
        assertEquals(exSingle.getBlockedMoves(), exDup.getBlockedMoves());
        assertEquals(exSingle.getInvalidCommands().size(), exDup.getInvalidCommands().size());

        // and: same visited path
        assertEquals(respSingle.getVisited(), respDup.getVisited());
    }
}
