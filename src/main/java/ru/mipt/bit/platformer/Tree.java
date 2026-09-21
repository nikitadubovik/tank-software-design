package ru.mipt.bit.platformer;

public class Tree {

    private final GridPosition position;

    public Tree(GridPosition position) {
        this.position = position;
    }

    public GridPosition getPosition() {
        return position;
    }
}