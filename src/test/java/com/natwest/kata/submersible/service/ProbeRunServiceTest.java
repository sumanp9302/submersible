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

import static org.junit.jupiter.api.Assertions.*;

class ProbeRunServiceTest {

    private final ProbeRunService service = new ProbeRunService();

    private RunRequest baseRequest() {
        RunRequest req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setStart(new CoordinateDto(1, 2, 3));
        req.setDirection(Direction.EAST);
        // default non-empty commands; tests will override when needed
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
}
