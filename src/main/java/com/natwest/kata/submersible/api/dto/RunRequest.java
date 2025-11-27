package com.natwest.kata.submersible.api.dto;

import com.natwest.kata.submersible.enums.Direction;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.List;

@Schema(description = "Input payload for executing a submersible probe run.")
public class RunRequest {

    @NotNull
    @Schema(
            description = "3D grid configuration where the probe operates.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private GridDto grid;

    @NotNull
    @Schema(
            description = "Starting coordinate of the probe.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private CoordinateDto start;

    @NotNull
    @Schema(
            description = "Initial facing direction of the probe.",
            example = "NORTH",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Direction direction;

    @NotNull
    @Schema(
            description = """
                    List of movement commands to execute, in order.
                    Supported commands:
                    - F: move forward
                    - B: move backward
                    - L: turn left
                    - R: turn right
                    - U: tilt up
                    - D: tilt down
                    """,
            example = "[\"F\", \"R\", \"F\", \"U\", \"F\"]",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<String> commands;

    @Schema(
            description = "List of obstacle coordinates that the probe must avoid.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
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
