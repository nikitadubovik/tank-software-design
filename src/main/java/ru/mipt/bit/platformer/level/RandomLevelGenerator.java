package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GridPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private final int width;
    private final int height;
    private final int treeCount;
    private final Random random;

    public RandomLevelGenerator(int width, int height, int treeCount, Random random) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Размеры поля должны быть положительными: " + width + "x" + height);
        }
        if (treeCount < 0 || treeCount > width * height - 1) {
            throw new IllegalArgumentException("Деревьев должно быть от 0 до " + (width * height - 1) + ": " + treeCount);
        }
        this.width = width;
        this.height = height;
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate() {
        List<GridPosition> cells = allCells();
        Collections.shuffle(cells, random);
        GridPosition playerStart = cells.get(0);
        List<GridPosition> trees = cells.subList(1, 1 + treeCount);
        return new LevelLayout(width, height, playerStart, trees);
    }

    private List<GridPosition> allCells() {
        List<GridPosition> cells = new ArrayList<>(width * height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPosition(x, y));
            }
        }
        return cells;
    }
}
