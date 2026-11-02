package ru.mipt.bit.platformer.model;

public final class BulletSettings {

    private final int damage;
    private final float cellDuration;

    public BulletSettings(int damage, float cellDuration) {
        if (damage < 0) {
            throw new IllegalArgumentException("Урон не может быть отрицательным: " + damage);
        }
        if (cellDuration <= 0f) {
            throw new IllegalArgumentException("Время пролёта клетки должно быть положительным: " + cellDuration);
        }
        this.damage = damage;
        this.cellDuration = cellDuration;
    }

    public int getDamage() {
        return damage;
    }

    public float getCellDuration() {
        return cellDuration;
    }
}
