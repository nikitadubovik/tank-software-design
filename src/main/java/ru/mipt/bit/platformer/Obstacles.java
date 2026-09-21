package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;

public class Obstacles implements ObstacleMap {

    private final List<Tree> trees = new ArrayList<>();

    public void add(Tree tree) {
        trees.add(tree);
    }

    @Override
    public boolean isFree(GridPosition position) {
        for (Tree tree : trees) {
            if (tree.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }
}