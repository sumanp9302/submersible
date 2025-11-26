
package com.natwest.kata.submersible.api.dto;

import jakarta.validation.constraints.Min;

public class GridDto {
    @Min(1) private int width;
    @Min(1) private int height;
    @Min(1) private int depth;

    public GridDto() { }
    public GridDto(int width, int height, int depth) {
        this.width = width; this.height = height; this.depth = depth;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return depth; }

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setDepth(int depth) { this.depth = depth; }
}
