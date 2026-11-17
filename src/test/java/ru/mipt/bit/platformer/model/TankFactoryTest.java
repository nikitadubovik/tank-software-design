package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankFactoryTest {

    private static final BulletSettings BULLET = new BulletSettings(25, 0.1f);

    private static TankFactory factory(int min, int max, long seed) {
        return new TankFactory(0.4f, min, max, BULLET, 0.5f, new Random(seed));
    }

    @Test
    void placesTankAtGivenPosition() {
        Tank tank = factory(80, 100, 1).create(new GridPosition(3, 4));

        assertEquals(new GridPosition(3, 4), tank.getPosition());
    }

    @Test
    void healthStaysWithinRange() {
        TankFactory factory = factory(80, 100, 1);

        for (int i = 0; i < 500; i++) {
            int health = factory.create(new GridPosition(0, 0)).getHealth();
            assertTrue(health >= 80 && health <= 100, "жизни вне диапазона: " + health);
        }
    }

    @Test
    void reachesBothEndsOfRange() {
        TankFactory factory = factory(80, 100, 2);
        Set<Integer> seen = new HashSet<>();

        for (int i = 0; i < 2000; i++) {
            seen.add(factory.create(new GridPosition(0, 0)).getHealth());
        }

        assertTrue(seen.contains(80));
        assertTrue(seen.contains(100));
    }

    @Test
    void sameHealthForEveryTankWhenRangeIsOneValue() {
        TankFactory factory = factory(100, 100, 3);

        assertEquals(100, factory.create(new GridPosition(0, 0)).getHealth());
        assertEquals(100, factory.create(new GridPosition(1, 0)).getMaxHealth());
    }

    @Test
    void createdTankIsFullyHealthy() {
        Tank tank = factory(80, 100, 4).create(new GridPosition(0, 0));

        assertEquals(tank.getMaxHealth(), tank.getHealth());
    }

    @Test
    void createdTankShootsBulletsWithGivenSettings() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = factory(80, 100, 5).create(new GridPosition(1, 1));
        level.add(tank);

        tank.shoot(level);

        assertEquals(25, ((Bullet) level.getObjects().get(1)).getDamage());
    }

    @Test
    void rejectsWrongRange() {
        assertThrows(IllegalArgumentException.class, () -> factory(100, 80, 1));
        assertThrows(IllegalArgumentException.class, () -> factory(-1, 80, 1));
    }
}
