
package com.natwest.kata.submersible.api.dto;

import java.util.List;

public class ExecutionDto {
    private int totalCommands;
    private int executedCommands;
    private int blockedMoves;
    private List<InvalidCommandDto> invalidCommands;

    public ExecutionDto() { }
    public ExecutionDto(int total, int executed, int blocked, List<InvalidCommandDto> invalids) {
        this.totalCommands = total; this.executedCommands = executed;
        this.blockedMoves = blocked; this.invalidCommands = invalids;
    }

    public int getTotalCommands() { return totalCommands; }
    public int getExecutedCommands() { return executedCommands; }
    public int getBlockedMoves() { return blockedMoves; }
    public List<InvalidCommandDto> getInvalidCommands() { return invalidCommands; }

    public void setTotalCommands(int totalCommands) { this.totalCommands = totalCommands; }
    public void setExecutedCommands(int executedCommands) { this.executedCommands = executedCommands; }
    public void setBlockedMoves(int blockedMoves) { this.blockedMoves = blockedMoves; }
    public void setInvalidCommands(List<InvalidCommandDto> invalidCommands) { this.invalidCommands = invalidCommands; }
}
