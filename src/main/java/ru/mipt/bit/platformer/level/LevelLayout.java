package ru.mipt.bit.platformer.level;

import ru.mipt.bit.platformer.model.GridPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class LevelLayout {

    private final int width;
    private final int height;
    private final GridPosition playerStart;
    private final List<GridPosition> treePositions;
    private final List<GridPosition> enemyTankPositions;

    public LevelLayout(int width, int height, GridPosition playerStart, List<GridPosition> treePositions) {
        this(width, height, playerStart, treePositions, Collections.emptyList());
    }

    public LevelLayout(int width, int height, GridPosition playerStart,
                       List<GridPosition> treePositions, List<GridPosition> enemyTankPositions) {
        this.width = width;
        this.height = height;
        this.playerStart = playerStart;
        this.treePositions = Collections.unmodifiableList(new ArrayList<>(treePositions));
        this.enemyTankPositions = Collections.unmodifiableList(new ArrayList<>(enemyTankPositions));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPosition getPlayerStart() {
        return playerStart;
    }

    public List<GridPosition> getTreePositions() {
        return treePositions;
    }

    public List<GridPosition> getEnemyTankPositions() {
        return enemyTankPositions;
    }

    public LevelLayout withEnemyTanks(List<GridPosition> positions) {
        return new LevelLayout(width, height, playerStart, treePositions, positions);
    }
}
