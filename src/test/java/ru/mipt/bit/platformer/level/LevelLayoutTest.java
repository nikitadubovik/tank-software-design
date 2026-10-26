package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelLayoutTest {

    @Test
    void isNotAffectedByLaterChangesOfSourceList() {
        List<GridPosition> trees = new ArrayList<>();
        trees.add(new GridPosition(1, 1));
        LevelLayout layout = new LevelLayout(3, 3, new GridPosition(0, 0), trees);

        trees.add(new GridPosition(2, 2));

        assertEquals(1, layout.getTreePositions().size());
    }

    @Test
    void treeListCannotBeModified() {
        LevelLayout layout = new LevelLayout(3, 3, new GridPosition(0, 0), new ArrayList<>());

        assertThrows(UnsupportedOperationException.class,
                () -> layout.getTreePositions().add(new GridPosition(1, 1)));
    }

    @Test
    void hasNoEnemiesByDefault() {
        LevelLayout layout = new LevelLayout(3, 3, new GridPosition(0, 0), new ArrayList<>());

        assertTrue(layout.getEnemyTankPositions().isEmpty());
    }

    @Test
    void withEnemyTanksKeepsEverythingElse() {
        List<GridPosition> trees = new ArrayList<>();
        trees.add(new GridPosition(1, 1));
        LevelLayout layout = new LevelLayout(3, 4, new GridPosition(0, 0), trees);

        LevelLayout withEnemies = layout.withEnemyTanks(Collections.singletonList(new GridPosition(2, 2)));

        assertEquals(3, withEnemies.getWidth());
        assertEquals(4, withEnemies.getHeight());
        assertEquals(new GridPosition(0, 0), withEnemies.getPlayerStart());
        assertEquals(trees, withEnemies.getTreePositions());
        assertEquals(Collections.singletonList(new GridPosition(2, 2)), withEnemies.getEnemyTankPositions());
        assertTrue(layout.getEnemyTankPositions().isEmpty());
    }
}
