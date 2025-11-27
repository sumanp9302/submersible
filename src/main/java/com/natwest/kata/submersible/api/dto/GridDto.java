package com.natwest.kata.submersible.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "3D grid dimensions (width, height, depth) where the probe operates.")
public class GridDto {

    @Min(1)
    @Schema(description = "Width of the grid along the X axis. Must be >= 1.", example = "5")
    private int width;

    @Min(1)
    @Schema(description = "Height of the grid along the Y axis. Must be >= 1.", example = "5")
    private int height;

    @Min(1)
    @Schema(description = "Depth of the grid along the Z axis. Must be >= 1.", example = "5")
    private int depth;

    public GridDto() {
    }

    public GridDto(int width, int height, int depth) {
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
