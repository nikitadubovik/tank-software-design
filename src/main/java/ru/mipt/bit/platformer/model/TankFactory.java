package ru.mipt.bit.platformer.model;

import java.util.Random;

public class TankFactory {

    private final float movementDuration;
    private final int minHealth;
    private final int maxHealth;
    private final BulletSettings bulletSettings;
    private final float reloadDuration;
    private final Random random;

    public TankFactory(float movementDuration, int minHealth, int maxHealth,
                       BulletSettings bulletSettings, float reloadDuration, Random random) {
        if (minHealth < 0 || minHealth > maxHealth) {
            throw new IllegalArgumentException("Неверный диапазон жизней: " + minHealth + ".." + maxHealth);
        }
        this.movementDuration = movementDuration;
        this.minHealth = minHealth;
        this.maxHealth = maxHealth;
        this.bulletSettings = bulletSettings;
        this.reloadDuration = reloadDuration;
        this.random = random;
    }

    public Tank create(GridPosition position) {
        int health = minHealth + random.nextInt(maxHealth - minHealth + 1);
        return new Tank(position, movementDuration, health, bulletSettings, reloadDuration);
    }
}
