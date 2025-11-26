
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

        // Build grid
        GridDto g = req.getGrid();
        Grid grid = new Grid(g.getWidth(), g.getHeight(), g.getDepth());

        // Obstacles: validate via Grid (throws if out of bounds) and add
        req.getObstacles().forEach(o -> grid.addObstacle(o.getX(), o.getY(), o.getZ()));

        // Validate start position
        CoordinateDto s = req.getStart();
        if (!grid.isWithinBounds(s.getX(), s.getY(), s.getZ())) {
            throw new IllegalArgumentException("Start position is out of bounds");
        }

        // Initialize probe
        Probe probe = new Probe(s.getX(), s.getY(), s.getZ(), req.getDirection(), grid);

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
        resp.setGrid(new GridDto(g.getWidth(), g.getHeight(), g.getDepth()));
        return resp;
    }
}
