package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GridPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomEnemyTanksGenerator implements LevelGenerator {

    private final LevelGenerator source;
    private final int enemyCount;
    private final Random random;

    public RandomEnemyTanksGenerator(LevelGenerator source, int enemyCount, Random random) {
        if (enemyCount < 0) {
            throw new IllegalArgumentException("Число танков не может быть отрицательным: " + enemyCount);
        }
        this.source = source;
        this.enemyCount = enemyCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate() {
        LevelLayout layout = source.generate();
        List<GridPosition> freeCells = freeCells(layout);
        Collections.shuffle(freeCells, random);

        List<GridPosition> enemies = new ArrayList<>(layout.getEnemyTankPositions());
        enemies.addAll(freeCells.subList(0, Math.min(enemyCount, freeCells.size())));
        return layout.withEnemyTanks(enemies);
    }

    private static List<GridPosition> freeCells(LevelLayout layout) {
        Set<GridPosition> occupied = new HashSet<>(layout.getTreePositions());
        occupied.addAll(layout.getEnemyTankPositions());
        occupied.add(layout.getPlayerStart());

        List<GridPosition> free = new ArrayList<>();
        for (int x = 0; x < layout.getWidth(); x++) {
            for (int y = 0; y < layout.getHeight(); y++) {
                GridPosition cell = new GridPosition(x, y);
                if (!occupied.contains(cell)) {
                    free.add(cell);
                }
            }
        }
        return free;
    }
}
