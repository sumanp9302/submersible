
package com.natwest.kata.submersible.api.dto;

import com.natwest.kata.submersible.enums.Direction;

import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public class RunRequest {
    @NotNull private GridDto grid;
    @NotNull private CoordinateDto start;
    @NotNull private Direction direction;
    @NotNull private List<String> commands;
    private List<CoordinateDto> obstacles = Collections.emptyList();

    public GridDto getGrid() { return grid; }
    public void setGrid(GridDto grid) { this.grid = grid; }

    public CoordinateDto getStart() { return start; }
    public void setStart(CoordinateDto start) { this.start = start; }

    public Direction getDirection() { return direction; }
    public void setDirection(Direction direction) { this.direction = direction; }

    public List<String> getCommands() { return commands; }
    public void setCommands(List<String> commands) { this.commands = commands; }

    public List<CoordinateDto> getObstacles() { return obstacles; }
    public void setObstacles(List<CoordinateDto> obstacles) { this.obstacles = obstacles; }
}
