package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GridPositionTest {

    @Test
    void moveUpIncrementsY() {
        GridPosition start = new GridPosition(1, 1);
        assertEquals(new GridPosition(1, 2), start.move(Direction.UP));
    }

    @Test
    void moveLeftDecrementsX() {
        GridPosition start = new GridPosition(1, 1);
        assertEquals(new GridPosition(0, 1), start.move(Direction.LEFT));
    }

    @Test
    void moveDownDecrementsY() {
        GridPosition start = new GridPosition(1, 1);
        assertEquals(new GridPosition(1, 0), start.move(Direction.DOWN));
    }

    @Test
    void moveRightIncrementsX() {
        GridPosition start = new GridPosition(1, 1);
        assertEquals(new GridPosition(2, 1), start.move(Direction.RIGHT));
    }

    @Test
    void moveDoesNotMutateOriginal() {
        GridPosition start = new GridPosition(1, 1);
        start.move(Direction.UP);
        assertEquals(new GridPosition(1, 1), start);
    }

    @Test
    void equalCoordinatesAreEqual() {
        assertEquals(new GridPosition(3, 4), new GridPosition(3, 4));
    }

    @Test
    void differentCoordinatesAreNotEqual() {
        assertNotEquals(new GridPosition(3, 4), new GridPosition(4, 3));
    }
}