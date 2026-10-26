package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomEnemyTanksGeneratorTest {

    private static final LevelLayout BASE = new LevelLayout(4, 3, new GridPosition(0, 0),
            Arrays.asList(new GridPosition(1, 1), new GridPosition(2, 2)));

    @Test
    void addsRequestedNumberOfTanks() {
        LevelLayout layout = new RandomEnemyTanksGenerator(() -> BASE, 3, new Random(1)).generate();

        assertEquals(3, layout.getEnemyTankPositions().size());
    }

    @Test
    void placesTanksOnlyOnFreeCellsInsideTheField() {
        for (long seed = 0; seed < 50; seed++) {
            LevelLayout layout = new RandomEnemyTanksGenerator(() -> BASE, 5, new Random(seed)).generate();
            List<GridPosition> enemies = layout.getEnemyTankPositions();
            Set<GridPosition> distinct = new HashSet<>(enemies);

            assertEquals(enemies.size(), distinct.size());
            assertFalse(distinct.contains(layout.getPlayerStart()));
            for (GridPosition enemy : enemies) {
                assertFalse(layout.getTreePositions().contains(enemy));
                assertTrue(enemy.getX() >= 0 && enemy.getX() < 4 && enemy.getY() >= 0 && enemy.getY() < 3);
            }
        }
    }

    @Test
    void keepsTheRestOfTheLevel() {
        LevelLayout layout = new RandomEnemyTanksGenerator(() -> BASE, 2, new Random(1)).generate();

        assertEquals(BASE.getWidth(), layout.getWidth());
        assertEquals(BASE.getHeight(), layout.getHeight());
        assertEquals(BASE.getPlayerStart(), layout.getPlayerStart());
        assertEquals(BASE.getTreePositions(), layout.getTreePositions());
    }

    @Test
    void placesNoMoreTanksThanFreeCells() {
        LevelLayout layout = new RandomEnemyTanksGenerator(() -> BASE, 100, new Random(1)).generate();

        assertEquals(4 * 3 - 3, layout.getEnemyTankPositions().size());
    }

    @Test
    void zeroTanksLeavesLevelWithoutEnemies() {
        LevelLayout layout = new RandomEnemyTanksGenerator(() -> BASE, 0, new Random(1)).generate();

        assertTrue(layout.getEnemyTankPositions().isEmpty());
    }

    @Test
    void keepsTanksAlreadyInTheLevel() {
        LevelLayout withEnemy = BASE.withEnemyTanks(Collections.singletonList(new GridPosition(3, 0)));

        LevelLayout layout = new RandomEnemyTanksGenerator(() -> withEnemy, 2, new Random(1)).generate();

        assertEquals(3, layout.getEnemyTankPositions().size());
        assertTrue(layout.getEnemyTankPositions().contains(new GridPosition(3, 0)));
        assertEquals(3, new HashSet<>(layout.getEnemyTankPositions()).size());
    }

    @Test
    void worksOnTopOfRandomLevel() {
        LevelGenerator generator = new RandomEnemyTanksGenerator(
                new RandomLevelGenerator(10, 8, 12, new Random(5)), 4, new Random(5));

        LevelLayout layout = generator.generate();

        assertEquals(4, layout.getEnemyTankPositions().size());
    }

    @Test
    void rejectsNegativeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomEnemyTanksGenerator(() -> BASE, -1, new Random()));
    }
}
