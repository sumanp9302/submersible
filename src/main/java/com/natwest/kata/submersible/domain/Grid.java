package com.natwest.kata.submersible.domain;

public class Grid {

    private final int width;
    private final int height;
    private final int depth;

    public Grid(int width, int height, int depth) {

        if(width<=0 || height <=0 || depth <=0){
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public boolean isWithinBounds(int x, int y, int z) {
        return x >= 0 && x < width && y >= 0 && y < height && z >= 0 && z < depth;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
