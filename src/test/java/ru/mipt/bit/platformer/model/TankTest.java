package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float DURATION = 0.4f;
    private static final ObstacleMap EVERYTHING_FREE = cell -> true;
    private static final ObstacleMap EVERYTHING_BLOCKED = cell -> false;

    @Test
    void startsAtInitialPositionAndIsNotMoving() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);

        assertEquals(new GridPosition(1, 1), tank.getPosition());
        assertFalse(tank.isMoving());
    }

    @Test
    void moveTowardsFreeCellStartsMovement() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);

        tank.move(Direction.UP, EVERYTHING_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveTowardsBlockedCellOnlyTurns() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);

        tank.move(Direction.UP, EVERYTHING_BLOCKED);

        assertFalse(tank.isMoving());
        assertEquals(new GridPosition(1, 1), tank.getPosition());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIsIgnoredWhileAlreadyMoving() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.move(Direction.RIGHT, EVERYTHING_FREE);

        assertEquals(new GridPosition(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void reachesDestinationAfterMovementDuration() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.update(DURATION);

        assertFalse(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getPosition());
    }

    @Test
    void staysOnStartCellUntilMovementIsFinished() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.update(DURATION / 4);

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 1), tank.getPosition());
    }

    @Test
    void movementDurationIsAParameter() {
        Tank slow = new Tank(new GridPosition(1, 1), 1f);
        Tank fast = new Tank(new GridPosition(1, 1), 0.25f);
        slow.move(Direction.UP, EVERYTHING_FREE);
        fast.move(Direction.UP, EVERYTHING_FREE);

        slow.update(0.5f);
        fast.update(0.5f);

        assertTrue(slow.isMoving());
        assertFalse(fast.isMoving());
    }

    @Test
    void occupiesBothCellsWhileMoving() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        tank.move(Direction.UP, EVERYTHING_FREE);

        assertTrue(tank.occupies(new GridPosition(1, 1)));
        assertTrue(tank.occupies(new GridPosition(1, 2)));
        assertFalse(tank.occupies(new GridPosition(2, 1)));
    }

    @Test
    void doesNotBlockItselfWhenRegisteredAsObstacle() {
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        Obstacles obstacles = new Obstacles();
        obstacles.add(tank);

        tank.move(Direction.UP, obstacles);

        assertTrue(tank.isMoving());
    }

    @Test
    void anotherTankBlocksTheWay() {
        Tank player = new Tank(new GridPosition(1, 1), DURATION);
        Tank enemy = new Tank(new GridPosition(1, 2), DURATION);
        Obstacles obstacles = new Obstacles();
        obstacles.add(player);
        obstacles.add(enemy);

        player.move(Direction.UP, obstacles);

        assertFalse(player.isMoving());
    }

    @Test
    void rejectsNonPositiveMovementDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPosition(0, 0), 0f));
    }

    @Test
    void cellBeingLeftStaysOccupiedForOtherTanks() {
        Tank leaving = new Tank(new GridPosition(1, 1), DURATION);
        Tank other = new Tank(new GridPosition(0, 1), DURATION);
        Obstacles obstacles = new Obstacles();
        obstacles.add(leaving);
        obstacles.add(other);

        leaving.move(Direction.UP, obstacles);
        other.move(Direction.RIGHT, obstacles);

        assertFalse(other.isMoving());
    }

    @Test
    void cellBeingEnteredIsOccupiedForOtherTanks() {
        Tank first = new Tank(new GridPosition(1, 1), DURATION);
        Tank second = new Tank(new GridPosition(1, 3), DURATION);
        Obstacles obstacles = new Obstacles();
        obstacles.add(first);
        obstacles.add(second);

        first.move(Direction.UP, obstacles);
        second.move(Direction.DOWN, obstacles);

        assertFalse(second.isMoving());
    }

    @Test
    void leftCellBecomesFreeAfterArrival() {
        Tank leaving = new Tank(new GridPosition(1, 1), DURATION);
        Tank other = new Tank(new GridPosition(0, 1), DURATION);
        Obstacles obstacles = new Obstacles();
        obstacles.add(leaving);
        obstacles.add(other);

        leaving.move(Direction.UP, obstacles);
        leaving.update(DURATION);
        other.move(Direction.RIGHT, obstacles);

        assertTrue(other.isMoving());
    }
}
