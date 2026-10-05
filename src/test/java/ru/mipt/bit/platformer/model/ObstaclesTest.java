package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObstaclesTest {

    @Test
    void emptyFieldIsFreeEverywhere() {
        assertTrue(new Obstacles().isFree(new GridPosition(1, 3)));
    }

    @Test
    void treeBlocksItsCellOnly() {
        Obstacles obstacles = new Obstacles();
        obstacles.add(new Tree(new GridPosition(1, 3)));

        assertFalse(obstacles.isFree(new GridPosition(1, 3)));
        assertTrue(obstacles.isFree(new GridPosition(1, 2)));
    }

    @Test
    void severalObstaclesAreAllTracked() {
        Obstacles obstacles = new Obstacles();
        obstacles.add(new Tree(new GridPosition(1, 3)));
        obstacles.add(new Tree(new GridPosition(4, 4)));

        assertFalse(obstacles.isFree(new GridPosition(1, 3)));
        assertFalse(obstacles.isFree(new GridPosition(4, 4)));
        assertTrue(obstacles.isFree(new GridPosition(4, 5)));
    }

    @Test
    void anyNewKindOfObstacleWorksWithoutChangingObstacles() {
        Obstacle wall = cell -> cell.getY() == 0;
        Obstacles obstacles = new Obstacles();
        obstacles.add(wall);

        assertFalse(obstacles.isFree(new GridPosition(7, 0)));
        assertTrue(obstacles.isFree(new GridPosition(7, 1)));
    }
}
