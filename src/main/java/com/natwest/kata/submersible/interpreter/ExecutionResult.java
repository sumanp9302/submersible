package com.natwest.kata.submersible.interpreter;

import java.util.List;

public class ExecutionResult {
    private final int totalCommands;
    private final int executedCommands;
    private final int blockedMoves;
    private final List<InvalidCommand> invalidCommands;

    public ExecutionResult(int totalCommands, int executedCommands, int blockedMoves, List<InvalidCommand> invalidCommands) {
        this.totalCommands = totalCommands;
        this.executedCommands = executedCommands;
        this.blockedMoves = blockedMoves;
        this.invalidCommands = invalidCommands;
    }

    public int getTotalCommands() {
        return totalCommands;
    }

    public int getExecutedCommands() {
        return executedCommands;
    }

    public int getBlockedMoves() {
        return blockedMoves;
    }

    public List<InvalidCommand> getInvalidCommands() {
        return invalidCommands;
    }
}
