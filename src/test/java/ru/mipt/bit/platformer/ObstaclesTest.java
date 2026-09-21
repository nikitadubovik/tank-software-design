package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstaclesTest {

    @Test
    void emptyMapIsFreeEverywhere() {
        Obstacles obstacles = new Obstacles();
        assertTrue(obstacles.isFree(new GridPosition(1, 3)));
    }

    @Test
    void occupiedTileIsNotFree() {
        Obstacles obstacles = new Obstacles();
        obstacles.add(new Tree(new GridPosition(1, 3)));

        assertFalse(obstacles.isFree(new GridPosition(1, 3)));
    }

    @Test
    void neighbouringTileStaysFree() {
        Obstacles obstacles = new Obstacles();
        obstacles.add(new Tree(new GridPosition(1, 3)));

        assertTrue(obstacles.isFree(new GridPosition(1, 2)));
    }

    @Test
    void severalTreesAreAllTracked() {
        Obstacles obstacles = new Obstacles();
        obstacles.add(new Tree(new GridPosition(1, 3)));
        obstacles.add(new Tree(new GridPosition(4, 4)));

        assertFalse(obstacles.isFree(new GridPosition(4, 4)));
        assertTrue(obstacles.isFree(new GridPosition(4, 5)));
    }
}