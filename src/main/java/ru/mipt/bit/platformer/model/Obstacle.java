package ru.mipt.bit.platformer.model;

public interface Obstacle {
    boolean occupies(GridPosition cell);
}
