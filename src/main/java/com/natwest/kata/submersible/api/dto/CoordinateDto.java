
package com.natwest.kata.submersible.api.dto;

import jakarta.validation.constraints.Min;

public class CoordinateDto {
    @Min(0) private int x;
    @Min(0) private int y;
    @Min(0) private int z;

    public CoordinateDto() { }
    public CoordinateDto(int x, int y, int z) {
        this.x = x; this.y = y; this.z = z;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }
}
