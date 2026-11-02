package ru.mipt.bit.platformer.model;

public class Tree implements GameObject {

    private final GridPosition position;

    public Tree(GridPosition position) {
        this.position = position;
    }

    public GridPosition getPosition() {
        return position;
    }

    @Override
    public boolean occupies(GridPosition cell) {
        return position.equals(cell);
    }

    @Override
    public void live(float deltaTime) {
    }
}
