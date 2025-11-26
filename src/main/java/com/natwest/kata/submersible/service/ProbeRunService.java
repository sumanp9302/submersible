package com.natwest.kata.submersible.service;

import com.natwest.kata.submersible.api.dto.*;
import com.natwest.kata.submersible.domain.Grid;
import com.natwest.kata.submersible.domain.Probe;
import com.natwest.kata.submersible.interpreter.CommandInterpreter;
import com.natwest.kata.submersible.interpreter.ExecutionResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProbeRunService {

    public RunResponse run(RunRequest req) {
        Objects.requireNonNull(req, "RunRequest cannot be null");
        Objects.requireNonNull(req.getGrid(), "Grid cannot be null");
        Objects.requireNonNull(req.getStart(), "Start position cannot be null");
        Objects.requireNonNull(req.getDirection(), "Direction cannot be null");
        Objects.requireNonNull(req.getCommands(), "Commands cannot be null");

        // Build grid from DTO
        GridDto gridDto = req.getGrid();
        Grid grid = new Grid(gridDto.getWidth(), gridDto.getHeight(), gridDto.getDepth());

        // Obstacles: validate via Grid (throws if out of bounds) and add
        if (req.getObstacles() != null) {
            req.getObstacles().forEach(o -> grid.addObstacle(o.getX(), o.getY(), o.getZ()));
        }

        // Validate start position
        CoordinateDto start = req.getStart();
        if (!grid.isWithinBounds(start.getX(), start.getY(), start.getZ())) {
            throw new IllegalArgumentException("Start position is out of bounds");
        }

        // Initialize probe
        Probe probe = new Probe(start.getX(), start.getY(), start.getZ(), req.getDirection(), grid);

        // Execute commands via interpreter
        CommandInterpreter interpreter = new CommandInterpreter();
        ExecutionResult er = interpreter.execute(req.getCommands(), probe);

        // Assemble response
        RunResponse resp = new RunResponse();
        resp.setFinalState(new FinalStateDto(probe.getX(), probe.getY(), probe.getZ(), probe.getDirection()));
        resp.setVisited(List.copyOf(probe.getVisitedCoordinates()));
        resp.setExecution(new ExecutionDto(
                er.getTotalCommands(),
                er.getExecutedCommands(),
                er.getBlockedMoves(),
                er.getInvalidCommands().stream()
                        .map(ic -> new InvalidCommandDto(ic.getIndex(), ic.getCommand(), ic.getReason()))
                        .toList()
        ));
        resp.setGrid(new GridDto(gridDto.getWidth(), gridDto.getHeight(), gridDto.getDepth()));

        // Human-readable summary
        resp.setSummary(buildExecutionSummary(probe, er, grid));

        return resp;
    }

    private String buildExecutionSummary(Probe probe, ExecutionResult er, Grid grid) {
        int visitedCount = probe.getVisitedCoordinates().size();
        int invalidCount = er.getInvalidCommands().size();

        return String.format(
                "Visited %d positions on a %dx%dx%d grid. Final position: (%d,%d,%d) facing %s. " +
                        "Total commands: %d, executed: %d, blocked moves: %d, invalid commands: %d.",
                visitedCount,
                grid.getWidth(), grid.getHeight(), grid.getDepth(),
                probe.getX(), probe.getY(), probe.getZ(), probe.getDirection(),
                er.getTotalCommands(), er.getExecutedCommands(), er.getBlockedMoves(), invalidCount
        );
    }
}
