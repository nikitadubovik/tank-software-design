package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    @Test
    void createsRequestedNumberOfTrees() {
        LevelLayout layout = new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(1)).generate();

        assertEquals(12, layout.getTreePositions().size());
    }

    @Test
    void keepsEverythingInsideTheField() {
        for (long seed = 0; seed < 50; seed++) {
            LevelLayout layout = new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(seed)).generate();

            assertInside(layout.getPlayerStart());
            for (GridPosition tree : layout.getTreePositions()) {
                assertInside(tree);
            }
        }
    }

    @Test
    void neverPutsTwoObjectsOnOneCell() {
        for (long seed = 0; seed < 50; seed++) {
            LevelLayout layout = new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(seed)).generate();
            List<GridPosition> trees = layout.getTreePositions();
            Set<GridPosition> distinctTrees = new HashSet<>(trees);

            assertEquals(trees.size(), distinctTrees.size());
            assertFalse(distinctTrees.contains(layout.getPlayerStart()));
        }
    }

    @Test
    void sameSeedGivesSameLevel() {
        LevelLayout first = new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(42)).generate();
        LevelLayout second = new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(42)).generate();

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTreePositions(), second.getTreePositions());
    }

    @Test
    void differentSeedsGiveDifferentLevels() {
        Set<GridPosition> starts = new HashSet<>();
        for (long seed = 0; seed < 20; seed++) {
            starts.add(new RandomLevelGenerator(WIDTH, HEIGHT, 12, new Random(seed)).generate().getPlayerStart());
        }

        assertTrue(starts.size() > 1);
    }

    @Test
    void canFillEveryCellExceptPlayer() {
        LevelLayout layout = new RandomLevelGenerator(3, 2, 5, new Random(7)).generate();

        assertEquals(5, layout.getTreePositions().size());
    }

    @Test
    void reportsFieldSize() {
        LevelLayout layout = new RandomLevelGenerator(WIDTH, HEIGHT, 0, new Random(3)).generate();

        assertEquals(WIDTH, layout.getWidth());
        assertEquals(HEIGHT, layout.getHeight());
    }

    @Test
    void rejectsMoreTreesThanFreeCells() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, 6, new Random()));
    }

    @Test
    void rejectsNegativeTreeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(3, 2, -1, new Random()));
    }

    @Test
    void rejectsEmptyField() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(0, 5, 0, new Random()));
    }

    private static void assertInside(GridPosition cell) {
        assertTrue(cell.getX() >= 0 && cell.getX() < WIDTH, "x вне поля: " + cell);
        assertTrue(cell.getY() >= 0 && cell.getY() < HEIGHT, "y вне поля: " + cell);
    }
}
