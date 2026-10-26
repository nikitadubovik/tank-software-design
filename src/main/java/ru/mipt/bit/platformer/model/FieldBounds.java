package ru.mipt.bit.platformer.model;

public class FieldBounds implements Obstacle {

    private final int width;
    private final int height;

    public FieldBounds(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Размеры поля должны быть положительными: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean occupies(GridPosition cell) {
        return cell.getX() < 0 || cell.getX() >= width || cell.getY() < 0 || cell.getY() >= height;
    }
}
