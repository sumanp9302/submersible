package com.natwest.kata.submersible.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Execution statistics and invalid-command details for a probe run.")
public class ExecutionDto {

    @Schema(description = "Total number of commands provided in the request.", example = "10")
    private int totalCommands;

    @Schema(description = "Number of commands that were successfully executed.", example = "8")
    private int executedCommands;

    @Schema(description = "Number of movement commands that were blocked (by obstacles or bounds).", example = "2")
    private int blockedMoves;

    @Schema(description = "List of invalid commands encountered during interpretation.")
    private List<InvalidCommandDto> invalidCommands;

    public ExecutionDto() {
        // default constructor for Jackson / frameworks
    }

    public ExecutionDto(int totalCommands, int executedCommands, int blockedMoves, List<InvalidCommandDto> invalidCommands) {
        this.totalCommands = totalCommands;
        this.executedCommands = executedCommands;
        this.blockedMoves = blockedMoves;
        this.invalidCommands = invalidCommands;
    }

    public int getTotalCommands() {
        return totalCommands;
    }

    public void setTotalCommands(int totalCommands) {
        this.totalCommands = totalCommands;
    }

    public int getExecutedCommands() {
        return executedCommands;
    }

    public void setExecutedCommands(int executedCommands) {
        this.executedCommands = executedCommands;
    }

    public int getBlockedMoves() {
        return blockedMoves;
    }

    public void setBlockedMoves(int blockedMoves) {
        this.blockedMoves = blockedMoves;
    }

    public List<InvalidCommandDto> getInvalidCommands() {
        return invalidCommands;
    }

    public void setInvalidCommands(List<InvalidCommandDto> invalidCommands) {
        this.invalidCommands = invalidCommands;
    }
}
