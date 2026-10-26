package ru.mipt.bit.platformer.model;

import java.util.Objects;

public final class GridPosition {

    private final int x;
    private final int y;

    public GridPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public GridPosition move(Direction direction) {
        return new GridPosition(x + direction.getDeltaX(), y + direction.getDeltaY());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GridPosition)) {
            return false;
        }
        GridPosition that = (GridPosition) other;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
