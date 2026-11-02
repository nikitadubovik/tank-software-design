package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTest {

    private static final float CELL = 0.1f;
    private static final BulletSettings SETTINGS = new BulletSettings(1, CELL);

    private static Tank tank(int x, int y) {
        return new Tank(new GridPosition(x, y), 0.4f, 2, SETTINGS, 0.5f);
    }

    @Test
    void fliesAlongItsDirection() {
        GameLevel level = new GameLevel(10, 8);
        Bullet bullet = new Bullet(level, new GridPosition(1, 1), Direction.RIGHT, SETTINGS);
        level.add(bullet);

        level.tick(0f);
        level.tick(CELL);
        level.tick(0f);

        assertEquals(new GridPosition(2, 1), bullet.getPosition());
        assertEquals(new GridPosition(3, 1), bullet.getDestination());
    }

    @Test
    void occupiesBothCellsWhileFlying() {
        GameLevel level = new GameLevel(10, 8);
        Bullet bullet = new Bullet(level, new GridPosition(1, 1), Direction.UP, SETTINGS);
        level.add(bullet);

        level.tick(0f);

        assertTrue(bullet.occupies(new GridPosition(1, 1)));
        assertTrue(bullet.occupies(new GridPosition(1, 2)));
    }

    @Test
    void disappearsAtTheEdge() {
        GameLevel level = new GameLevel(3, 3);
        Bullet bullet = new Bullet(level, new GridPosition(2, 1), Direction.RIGHT, SETTINGS);
        level.add(bullet);

        level.tick(0f);

        assertFalse(level.contains(bullet));
    }

    @Test
    void disappearsOnTree() {
        GameLevel level = new GameLevel(10, 8);
        Tree tree = new Tree(new GridPosition(3, 1));
        Bullet bullet = new Bullet(level, new GridPosition(1, 1), Direction.RIGHT, SETTINGS);
        level.add(tree);
        level.add(bullet);

        for (int i = 0; i < 10; i++) {
            level.tick(CELL);
        }

        assertFalse(level.contains(bullet));
        assertTrue(level.contains(tree));
    }

    @Test
    void damagesTankAndDisappears() {
        GameLevel level = new GameLevel(10, 8);
        Tank target = tank(4, 1);
        Bullet bullet = new Bullet(level, new GridPosition(1, 1), Direction.RIGHT, SETTINGS);
        level.add(target);
        level.add(bullet);

        for (int i = 0; i < 10; i++) {
            level.tick(CELL);
        }

        assertFalse(level.contains(bullet));
        assertEquals(1, target.getHealth());
    }

    @Test
    void hitsTankStandingRightWhereItAppears() {
        GameLevel level = new GameLevel(10, 8);
        Tank target = tank(2, 1);
        Bullet bullet = new Bullet(level, new GridPosition(2, 1), Direction.RIGHT, SETTINGS);
        level.add(target);
        level.add(bullet);

        level.tick(0f);

        assertFalse(level.contains(bullet));
        assertEquals(1, target.getHealth());
    }

    @Test
    void hitsTankThatDrivesIntoItsPath() {
        GameLevel level = new GameLevel(10, 8);
        Bullet bullet = new Bullet(level, new GridPosition(1, 1), Direction.UP, SETTINGS);
        level.add(bullet);
        level.tick(0f);
        Tank target = tank(2, 2);
        level.add(target);
        target.move(Direction.LEFT, cell -> true);

        level.tick(0f);

        assertFalse(level.contains(bullet));
        assertEquals(1, target.getHealth());
    }

    @Test
    void twoBulletsDestroyEachOther() {
        GameLevel level = new GameLevel(10, 8);
        Bullet left = new Bullet(level, new GridPosition(1, 1), Direction.RIGHT, SETTINGS);
        Bullet right = new Bullet(level, new GridPosition(5, 1), Direction.LEFT, SETTINGS);
        level.add(left);
        level.add(right);

        for (int i = 0; i < 10; i++) {
            level.tick(CELL);
        }

        assertFalse(level.contains(left));
        assertFalse(level.contains(right));
    }

    @Test
    void tankIsRemovedAfterEnoughHits() {
        GameLevel level = new GameLevel(10, 8);
        Tank target = tank(4, 1);
        level.add(target);

        for (int shot = 0; shot < 3; shot++) {
            level.add(new Bullet(level, new GridPosition(1, 1), Direction.RIGHT, SETTINGS));
            for (int i = 0; i < 10; i++) {
                level.tick(CELL);
            }
        }

        assertTrue(target.isDestroyed());
        assertFalse(level.contains(target));
    }

    @Test
    void damageComesFromSettings() {
        GameLevel level = new GameLevel(10, 8);
        Tank target = tank(2, 1);
        level.add(target);
        level.add(new Bullet(level, new GridPosition(2, 1), Direction.RIGHT, new BulletSettings(5, CELL)));

        level.tick(0f);

        assertFalse(level.contains(target));
    }

    @Test
    void settingsRejectWrongValues() {
        assertThrows(IllegalArgumentException.class, () -> new BulletSettings(-1, CELL));
        assertThrows(IllegalArgumentException.class, () -> new BulletSettings(1, 0f));
    }
}
