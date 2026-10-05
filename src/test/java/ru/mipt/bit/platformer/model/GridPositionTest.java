package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class GridPositionTest {

    @Test
    void moveUpIncrementsY() {
        assertEquals(new GridPosition(1, 2), new GridPosition(1, 1).move(Direction.UP));
    }

    @Test
    void moveLeftDecrementsX() {
        assertEquals(new GridPosition(0, 1), new GridPosition(1, 1).move(Direction.LEFT));
    }

    @Test
    void moveDownDecrementsY() {
        assertEquals(new GridPosition(1, 0), new GridPosition(1, 1).move(Direction.DOWN));
    }

    @Test
    void moveRightIncrementsX() {
        assertEquals(new GridPosition(2, 1), new GridPosition(1, 1).move(Direction.RIGHT));
    }

    @Test
    void moveDoesNotChangeOriginal() {
        GridPosition start = new GridPosition(1, 1);
        start.move(Direction.UP);
        assertEquals(new GridPosition(1, 1), start);
    }

    @Test
    void exposesCoordinates() {
        GridPosition cell = new GridPosition(3, 4);
        assertEquals(3, cell.getX());
        assertEquals(4, cell.getY());
    }

    @Test
    void equalCoordinatesAreEqual() {
        assertEquals(new GridPosition(3, 4), new GridPosition(3, 4));
        assertEquals(new GridPosition(3, 4).hashCode(), new GridPosition(3, 4).hashCode());
    }

    @Test
    void differentCoordinatesAreNotEqual() {
        assertNotEquals(new GridPosition(3, 4), new GridPosition(4, 3));
    }
}
