package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {

    @Test
    void upPointsAlongPositiveY() {
        assertEquals(0, Direction.UP.getDeltaX());
        assertEquals(1, Direction.UP.getDeltaY());
        assertEquals(90f, Direction.UP.getAngle());
    }

    @Test
    void leftPointsAlongNegativeX() {
        assertEquals(-1, Direction.LEFT.getDeltaX());
        assertEquals(0, Direction.LEFT.getDeltaY());
        assertEquals(-180f, Direction.LEFT.getAngle());
    }

    @Test
    void downPointsAlongNegativeY() {
        assertEquals(0, Direction.DOWN.getDeltaX());
        assertEquals(-1, Direction.DOWN.getDeltaY());
        assertEquals(-90f, Direction.DOWN.getAngle());
    }

    @Test
    void rightPointsAlongPositiveX() {
        assertEquals(1, Direction.RIGHT.getDeltaX());
        assertEquals(0, Direction.RIGHT.getDeltaY());
        assertEquals(0f, Direction.RIGHT.getAngle());
    }
}
