package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FieldBoundsTest {

    private final FieldBounds bounds = new FieldBounds(10, 8);

    @Test
    void cellsInsideAreFree() {
        assertFalse(bounds.occupies(new GridPosition(0, 0)));
        assertFalse(bounds.occupies(new GridPosition(9, 7)));
        assertFalse(bounds.occupies(new GridPosition(4, 3)));
    }

    @Test
    void cellsBeyondEachEdgeAreOccupied() {
        assertTrue(bounds.occupies(new GridPosition(-1, 3)));
        assertTrue(bounds.occupies(new GridPosition(10, 3)));
        assertTrue(bounds.occupies(new GridPosition(4, -1)));
        assertTrue(bounds.occupies(new GridPosition(4, 8)));
    }

    @Test
    void rejectsEmptyField() {
        assertThrows(IllegalArgumentException.class, () -> new FieldBounds(0, 8));
    }

    @Test
    void tankStopsAtTheEdgeLikeAtAnObstacle() {
        Tank tank = new Tank(new GridPosition(0, 0), 0.4f);
        Obstacles obstacles = new Obstacles();
        obstacles.add(bounds);
        obstacles.add(tank);

        tank.move(Direction.LEFT, obstacles);

        assertFalse(tank.isMoving());
        assertEquals(Direction.LEFT, tank.getDirection());
    }
}
