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
    private static final BulletSettings BULLET = new BulletSettings(1, 0.1f);

    private static Tank tank(int x, int y) {
        return new Tank(new GridPosition(x, y), DURATION, 2, BULLET, 0.5f);
    }

    private static long bullets(GameLevel level) {
        return level.getObjects().stream().filter(o -> o instanceof Bullet).count();
    }

    @Test
    void startsAtInitialPositionAndIsNotMoving() {
        Tank tank = tank(1, 1);

        assertEquals(new GridPosition(1, 1), tank.getPosition());
        assertFalse(tank.isMoving());
    }

    @Test
    void moveTowardsFreeCellStartsMovement() {
        Tank tank = tank(1, 1);

        tank.move(Direction.UP, EVERYTHING_FREE);

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveTowardsBlockedCellOnlyTurns() {
        Tank tank = tank(1, 1);

        tank.move(Direction.UP, EVERYTHING_BLOCKED);

        assertFalse(tank.isMoving());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void moveIsIgnoredWhileAlreadyMoving() {
        Tank tank = tank(1, 1);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.move(Direction.RIGHT, EVERYTHING_FREE);

        assertEquals(new GridPosition(1, 2), tank.getDestination());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void reachesDestinationAfterMovementDuration() {
        Tank tank = tank(1, 1);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.live(DURATION);

        assertFalse(tank.isMoving());
        assertEquals(new GridPosition(1, 2), tank.getPosition());
    }

    @Test
    void staysOnStartCellUntilMovementIsFinished() {
        Tank tank = tank(1, 1);
        tank.move(Direction.UP, EVERYTHING_FREE);

        tank.live(DURATION / 4);

        assertTrue(tank.isMoving());
        assertEquals(new GridPosition(1, 1), tank.getPosition());
    }

    @Test
    void occupiesBothCellsWhileMoving() {
        Tank tank = tank(1, 1);
        tank.move(Direction.UP, EVERYTHING_FREE);

        assertTrue(tank.occupies(new GridPosition(1, 1)));
        assertTrue(tank.occupies(new GridPosition(1, 2)));
        assertFalse(tank.occupies(new GridPosition(2, 1)));
    }

    @Test
    void cellBeingLeftStaysOccupiedForOtherTanks() {
        GameLevel level = new GameLevel(10, 8);
        Tank leaving = tank(1, 1);
        Tank other = tank(0, 1);
        level.add(leaving);
        level.add(other);

        leaving.move(Direction.UP, level);
        other.move(Direction.RIGHT, level);

        assertFalse(other.isMoving());
    }

    @Test
    void cellBeingEnteredIsOccupiedForOtherTanks() {
        GameLevel level = new GameLevel(10, 8);
        Tank first = tank(1, 1);
        Tank second = tank(1, 3);
        level.add(first);
        level.add(second);

        first.move(Direction.UP, level);
        second.move(Direction.DOWN, level);

        assertFalse(second.isMoving());
    }

    @Test
    void doesNotBlockItself() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);

        tank.move(Direction.UP, level);

        assertTrue(tank.isMoving());
    }

    @Test
    void shotCreatesBulletInFrontOfTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);
        tank.move(Direction.UP, EVERYTHING_BLOCKED);

        tank.shoot(level);

        Bullet bullet = (Bullet) level.getObjects().get(1);
        assertEquals(new GridPosition(1, 2), bullet.getPosition());
        assertEquals(Direction.UP, bullet.getDirection());
        assertEquals(BULLET.getDamage(), bullet.getDamage());
    }

    @Test
    void cannotShootAgainWhileReloading() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);

        tank.shoot(level);
        tank.shoot(level);

        assertEquals(1, bullets(level));
        assertTrue(tank.isReloading());
    }

    @Test
    void canShootAgainAfterReload() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);

        tank.shoot(level);
        tank.live(0.5f);
        tank.shoot(level);

        assertEquals(2, bullets(level));
    }

    @Test
    void cannotShootWhileMoving() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);
        tank.move(Direction.UP, level);

        tank.shoot(level);

        assertEquals(0, bullets(level));
    }

    @Test
    void shotIntoTheEdgeCreatesNoBullet() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(9, 1);
        level.add(tank);

        tank.shoot(level);

        assertEquals(0, bullets(level));
    }

    @Test
    void survivesWhileHealthIsNotNegative() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);

        tank.onHit(1, level);
        tank.onHit(1, level);

        assertEquals(0, tank.getHealth());
        assertFalse(tank.isDestroyed());
        assertTrue(level.contains(tank));
    }

    @Test
    void isRemovedWhenHealthBecomesNegative() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);

        tank.onHit(3, level);

        assertTrue(tank.isDestroyed());
        assertFalse(level.contains(tank));
    }

    @Test
    void destroyedTankIgnoresCommands() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = tank(1, 1);
        level.add(tank);
        tank.onHit(3, level);

        tank.move(Direction.UP, EVERYTHING_FREE);
        tank.shoot(level);

        assertFalse(tank.isMoving());
        assertEquals(0, bullets(level));
    }

    @Test
    void rejectsNonPositiveMovementDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPosition(0, 0), 0f));
    }

    @Test
    void rejectsNegativeHealth() {
        assertThrows(IllegalArgumentException.class,
                () -> new Tank(new GridPosition(0, 0), DURATION, -1, BULLET, 0.5f));
    }
}
