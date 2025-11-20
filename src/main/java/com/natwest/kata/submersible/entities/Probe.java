package com.natwest.kata.submersible.entities;

import com.natwest.kata.submersible.enums.Direction;


public class Probe {

    private int x;
    private int y;
    private int z;
    private Direction direction;


    public Probe(int x, int y, int z, Direction direction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public Direction getDirection() { return direction; }

    public void moveForward()
    {
        switch (direction){
            case NORTH -> y++;
            case SOUTH -> y--;
            case EAST -> x++;
            case WEST -> x--;
        }
    }

    public void moveBackward()
    {
        switch (direction){
            case NORTH -> y--;
            case SOUTH -> y++;
            case EAST -> x--;
            case WEST -> x++;
        }
    }

    public void turnLeft(){
        switch(direction){
            case NORTH -> direction = Direction.WEST;
            case WEST -> direction = Direction.SOUTH;
            case SOUTH -> direction = Direction.EAST;
            case EAST -> direction = Direction.NORTH;
        }
    }

}
