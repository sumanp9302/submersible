package com.natwest.kata.submersible.interpreter;

public class InvalidCommand {
    private final int index;
    private final String command;
    private final String reason;

    public InvalidCommand(int index, String command, String reason) {
        this.index = index;
        this.command = command;
        this.reason = reason;
    }

    public int getIndex() {
        return index;
    }

    public String getCommand() {
        return command;
    }

    public String getReason() {
        return reason;
    }

}
