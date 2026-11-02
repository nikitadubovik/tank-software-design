package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveCommandTest {

    private static final ObstacleMap EVERYTHING_FREE = cell -> true;
    private static final ObstacleMap EVERYTHING_BLOCKED = cell -> false;

    @Test
    void doesNothingUntilExecuted() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);

        new MoveCommand(tank, Direction.UP, EVERYTHING_FREE);

        assertFalse(tank.isMoving());
    }

    @Test
    void movesTankWhenExecuted() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);

        new MoveCommand(tank, Direction.UP, EVERYTHING_FREE).execute();

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getDestination());
    }

    @Test
    void respectsObstacles() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);

        new MoveCommand(tank, Direction.UP, EVERYTHING_BLOCKED).execute();

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void canBeExecutedAgainForTheNextCell() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        MoveCommand moveUp = new MoveCommand(tank, Direction.UP, EVERYTHING_FREE);

        moveUp.execute();
        tank.live(0.4f);
        moveUp.execute();
        tank.live(0.4f);

        assertEquals(new GridPosition(1, 3), tank.getPosition());
    }

    @Test
    void remembersItsDirection() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);

        assertEquals(Direction.LEFT, new MoveCommand(tank, Direction.LEFT, EVERYTHING_FREE).getDirection());
    }
}
