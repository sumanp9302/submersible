package com.natwest.kata.submersible.domain;

import com.natwest.kata.submersible.enums.Direction;

import java.util.ArrayList;
import java.util.List;


public class Probe {

    private final Grid grid;
    private final List<String> visitedCoordinates = new ArrayList<>();
    private int x;
    private int y;
    private int z;
    private Direction direction;
    private Direction lastHorizontalDirection;

    public Probe(int x, int y, int z, Direction direction, Grid grid) {
        if (direction == null) throw new IllegalArgumentException("Direction cannot be null");
        if (!grid.isWithinBounds(x, y, z)) throw new IllegalArgumentException("Position out of bounds");
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.grid = grid;
        this.lastHorizontalDirection = direction;
        recordPosition();
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

    public List<String> getVisitedCoordinates() {
        return visitedCoordinates;
    }

    public void moveForward() {
        switch (direction) {
            case NORTH -> move(0, 1, 0);
            case SOUTH -> move(0, -1, 0);
            case EAST -> move(1, 0, 0);
            case WEST -> move(-1, 0, 0);
            case UP -> move(0, 0, 1);
            case DOWN -> move(0, 0, -1);
        }
    }


    public void moveBackward() {
        switch (direction) {
            case NORTH -> move(0, -1, 0);
            case SOUTH -> move(0, 1, 0);
            case EAST -> move(-1, 0, 0);
            case WEST -> move(1, 0, 0);
            case UP -> move(0, 0, -1);
            case DOWN -> move(0, 0, 1);
        }
    }


    public void moveUp() {
        move(0, 0, 1);
    }

    public void moveDown() {
        move(0, 0, -1);
    }

    public void turnUp() {
        if (direction == Direction.UP) return;
        if (direction == Direction.DOWN) {
            direction = lastHorizontalDirection;
            return;
        }
        // horizontal → UP
        lastHorizontalDirection = direction;
        direction = Direction.UP;
    }

    public void turnDown() {
        if (direction == Direction.DOWN) return;
        if (direction == Direction.UP) {
            direction = lastHorizontalDirection;
            return;
        }

        // horizontal → DOWN
        lastHorizontalDirection = direction;
        direction = Direction.DOWN;
    }

    public void turnLeft() {
        switch (direction) {
            case NORTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.NORTH;
            case UP, DOWN -> { /* NOOP */ }
        }
    }

    public void turnRight() {
        switch (direction) {
            case NORTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.NORTH;
            case UP, DOWN -> { /* NOOP */ }
        }
    }

    private void move(int dx, int dy, int dz) {
        int newX = x + dx, newY = y + dy, newZ = z + dz;
        if (grid.isWithinBounds(newX, newY, newZ) && !grid.isObstacle(newX, newY, newZ)) {
            x = newX;
            y = newY;
            z = newZ;
            recordPosition();
        }
    }

    public void recordPosition() {
        visitedCoordinates.add("(" + x + "," + y + "," + z + ")");
    }
}