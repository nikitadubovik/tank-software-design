package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.List;

public class Obstacles implements ObstacleMap {

    private final List<Obstacle> obstacles = new ArrayList<>();

    public void add(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    @Override
    public boolean isFree(GridPosition cell) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(cell)) {
                return false;
            }
        }
        return true;
    }
}
