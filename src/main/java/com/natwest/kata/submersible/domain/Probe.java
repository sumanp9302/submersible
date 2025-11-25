package com.natwest.kata.submersible.domain;

import com.natwest.kata.submersible.enums.Direction;


public class Probe {

    private int x;
    private int y;
    private int z;
    private Direction direction;
    private final Grid grid;

    public Probe(int x, int y, int z, Direction direction, Grid grid) {
        if (direction == null) throw new IllegalArgumentException("Direction cannot be null");
        if (!grid.isWithinBounds(x, y, z)) throw new IllegalArgumentException("Position out of bounds");
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.grid = grid;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Direction getDirection() {
        return direction;
    }

    public void moveForward() {
        move(direction == Direction.NORTH ? 0 : direction == Direction.SOUTH ? 0 :
                        direction == Direction.EAST ? 1 : -1,
                direction == Direction.NORTH ? 1 : direction == Direction.SOUTH ? -1 : 0, 0);
    }

    public void moveBackward() {
        move(direction == Direction.NORTH ? 0 : direction == Direction.SOUTH ? 0 :
                        direction == Direction.EAST ? -1 : 1,
                direction == Direction.NORTH ? -1 : direction == Direction.SOUTH ? 1 : 0, 0);
    }

    public void moveUp() {
        move(0, 0, 1);
    }

    public void moveDown() {
        move(0, 0, -1);
    }

    public void turnLeft() {
        switch (direction) {
            case NORTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.NORTH;
        }
    }

    public void turnRight() {
        switch (direction) {
            case NORTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.NORTH;
        }
    }


    private void move(int dx, int dy, int dz) {
        int newX = x + dx, newY = y + dy, newZ = z + dz;
        if (grid.isWithinBounds(newX, newY, newZ) && !grid.isObstacle(newX, newY, newZ)) {
            x = newX; y = newY; z = newZ;
        }
    }

}