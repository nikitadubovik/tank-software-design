package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final class FakeObstacleMap implements ObstacleMap {
        private final boolean free;

        private FakeObstacleMap(boolean free) {
            this.free = free;
        }

        @Override
        public boolean isFree(GridPosition position) {
            return free;
        }
    }

    @Test
    void tankStartsAtInitialPositionAndIsNotMoving() {
        Tank tank = new Tank(new GridPosition(1, 1));

        assertEquals(new GridPosition(1, 1), tank.getPosition());
        assertFalse(tank.isMoving());
    }

    @Test
    void moveTowardsFreeTileStartsMovement() {
        Tank tank = new Tank(new GridPosition(1, 1));

        tank.move(Direction.UP, new FakeObstacleMap(true));

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getDestination());
    }

    @Test
    void moveTowardsBlockedTileOnlyRotates() {
        Tank tank = new Tank(new GridPosition(1, 1));

        tank.move(Direction.UP, new FakeObstacleMap(false));

        assertFalse(tank.isMoving());
        assertEquals(new GridPosition(1, 1), tank.getPosition());
        assertEquals(Direction.UP.getAngle(), tank.getAngle());
    }

    @Test
    void moveIsIgnoredWhileAlreadyMoving() {
        Tank tank = new Tank(new GridPosition(1, 1));
        tank.move(Direction.UP, new FakeObstacleMap(true));

        tank.move(Direction.RIGHT, new FakeObstacleMap(true));

        assertEquals(new GridPosition(1, 2), tank.getDestination());
    }

    @Test
    void updateReachesDestinationAfterEnoughTime() {
        Tank tank = new Tank(new GridPosition(1, 1));
        tank.move(Direction.UP, new FakeObstacleMap(true));

        tank.update(1f);

        assertFalse(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getPosition());
    }

    @Test
    void updateInSmallStepsKeepsTankMoving() {
        Tank tank = new Tank(new GridPosition(1, 1));
        tank.move(Direction.UP, new FakeObstacleMap(true));

        tank.update(0.1f);

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 1), tank.getPosition());
    }
}