package ru.mipt.bit.platformer;

public enum Direction {
    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int deltaX;
    private final int deltaY;
    private final float angle;

    Direction(int deltaX, int deltaY, float angle) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.angle = angle;
    }

    public int getDeltaX() {
        return deltaX;
    }

    public int getDeltaY() {
        return deltaY;
    }

    public float getAngle() {
        return angle;
    }
}