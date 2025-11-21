package com.natwest.kata.submersible.test;

import com.natwest.kata.submersible.domain.Grid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    @Test
    void shouldReturnTrueWhenPositionIsWithinBounds(){
        Grid grid = new Grid(5, 5, 5);

        // corners and some internal point
        assertTrue(grid.isWithinBounds(0, 0, 0));   // origin corner
        assertTrue(grid.isWithinBounds(4, 4, 4));   // opposite corner
        assertTrue(grid.isWithinBounds(2, 3, 1));   // somewhere in the middle
    }

    @Test
    void shouldReturnFalseWhenPositionIsOutsideBounds(){
        Grid grid = new Grid(5, 5, 5);

        // negative coordinates
        assertFalse(grid.isWithinBounds(-1, 0, 0));
        assertFalse(grid.isWithinBounds(0, -1, 0));
        assertFalse(grid.isWithinBounds(0, 0, -1));

        // equal to dimensions (just outside)
        assertFalse(grid.isWithinBounds(5, 0, 0));
        assertFalse(grid.isWithinBounds(0, 5, 0));
        assertFalse(grid.isWithinBounds(0, 0, 5));

        // way outside
        assertFalse(grid.isWithinBounds(6, 6, 6));
    }

}
