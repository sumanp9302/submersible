
package com.natwest.kata.submersible.api.dto;

import com.natwest.kata.submersible.enums.Direction;

public class FinalStateDto {
    private int x, y, z;
    private Direction direction;

    public FinalStateDto() { }
    public FinalStateDto(int x, int y, int z, Direction direction) {
        this.x = x; this.y = y; this.z = z; this.direction = direction;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public Direction getDirection() { return direction; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setZ(int z) { this.z = z; }
    public void setDirection(Direction direction) { this.direction = direction; }
}
