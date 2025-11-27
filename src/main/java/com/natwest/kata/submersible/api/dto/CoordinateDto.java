package com.natwest.kata.submersible.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "3D coordinate representing a position within the probe grid.")
public class CoordinateDto {

    @Min(0)
    @Schema(description = "X coordinate (width axis). Must be >= 0.", example = "2")
    private int x;

    @Min(0)
    @Schema(description = "Y coordinate (height axis). Must be >= 0.", example = "3")
    private int y;

    @Min(0)
    @Schema(description = "Z coordinate (depth axis). Must be >= 0.", example = "1")
    private int z;

    public CoordinateDto() {
    }

    public CoordinateDto(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
}
