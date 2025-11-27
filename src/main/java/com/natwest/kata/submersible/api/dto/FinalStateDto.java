package com.natwest.kata.submersible.api.dto;

import com.natwest.kata.submersible.enums.Direction;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Final state of the probe after executing all commands.")
public class FinalStateDto {

    @Schema(description = "Final X coordinate of the probe.", example = "2")
    private int x;

    @Schema(description = "Final Y coordinate of the probe.", example = "3")
    private int y;

    @Schema(description = "Final Z coordinate of the probe.", example = "1")
    private int z;

    @Schema(description = "Final facing direction of the probe.", example = "NORTH")
    private Direction direction;

    public FinalStateDto() {
    }

    public FinalStateDto(int x, int y, int z, Direction direction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
