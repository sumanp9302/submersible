package com.natwest.kata.submersible.domain;

import java.util.HashSet;
import java.util.Set;

public class Grid {

    private final int width;
    private final int height;
    private final int depth;
    private final Set<String> obstacles = new HashSet<>();

    public Grid(int width, int height, int depth) {

        if (width <= 0 || height <= 0 || depth <= 0) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public boolean isWithinBounds(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth;
    }


    public void addObstacle(int x, int y, int z) {
        if (!isWithinBounds(x, y, z)) throw new IllegalArgumentException("Obstacle out of bounds");
        obstacles.add(x + "," + y + "," + z);
    }

    public boolean isObstacle(int x, int y, int z) {
        return obstacles.contains(x + "," + y + "," + z);
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return depth; }
}
