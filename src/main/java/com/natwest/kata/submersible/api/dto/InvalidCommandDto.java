
package com.natwest.kata.submersible.api.dto;

public class InvalidCommandDto {
    private int index;
    private String command;
    private String reason;

    public InvalidCommandDto() { }
    public InvalidCommandDto(int index, String command, String reason) {
        this.index = index; this.command = command; this.reason = reason;
    }

    public int getIndex() { return index; }
    public String getCommand() { return command; }
    public String getReason() { return reason; }

    public void setIndex(int index) { this.index = index; }
    public void setCommand(String command) { this.command = command; }
    public void setReason(String reason) { this.reason = reason; }
}
