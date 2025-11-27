package com.natwest.kata.submersible.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a single invalid command encountered during execution.")
public class InvalidCommandDto {

    @Schema(description = "Zero-based index of the command in the original list.", example = "3")
    private int index;

    @Schema(description = "The raw command string that could not be interpreted.", example = "X")
    private String command;

    @Schema(description = "Explanation of why the command was considered invalid.", example = "Unknown command")
    private String reason;

    public InvalidCommandDto() {
    }

    public InvalidCommandDto(int index, String command, String reason) {
        this.index = index;
        this.command = command;
        this.reason = reason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
