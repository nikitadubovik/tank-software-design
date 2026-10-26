package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelParserTest {

    private static final List<String> EXAMPLE = Arrays.asList(
            "___T__T___",
            "__TT__TTTT",
            "_________T",
            "TTTT__T__T",
            "_____X____",
            "__________"
    );

    @Test
    void readsSizeFromText() {
        LevelLayout layout = LevelParser.parse(EXAMPLE);

        assertEquals(10, layout.getWidth());
        assertEquals(6, layout.getHeight());
    }

    @Test
    void findsPlayerStart() {
        assertEquals(new GridPosition(5, 1), LevelParser.parse(EXAMPLE).getPlayerStart());
    }

    @Test
    void findsEveryTree() {
        assertEquals(15, LevelParser.parse(EXAMPLE).getTreePositions().size());
    }

    @Test
    void topLineIsTopOfTheField() {
        List<GridPosition> trees = LevelParser.parse(EXAMPLE).getTreePositions();

        assertTrue(trees.contains(new GridPosition(3, 5)));
        assertTrue(trees.contains(new GridPosition(0, 2)));
        assertTrue(trees.contains(new GridPosition(9, 3)));
    }

    @Test
    void ignoresTrailingBlankLines() {
        LevelLayout layout = LevelParser.parse(Arrays.asList("X_", "_T", "", "  "));

        assertEquals(2, layout.getHeight());
    }

    @Test
    void ignoresByteOrderMark() {
        LevelLayout layout = LevelParser.parse(Arrays.asList("﻿X_", "_T"));

        assertEquals(new GridPosition(0, 1), layout.getPlayerStart());
    }

    @Test
    void rejectsLevelWithoutPlayer() {
        assertThrows(IllegalArgumentException.class, () -> LevelParser.parse(Arrays.asList("T_", "__")));
    }

    @Test
    void rejectsSeveralPlayers() {
        assertThrows(IllegalArgumentException.class, () -> LevelParser.parse(Arrays.asList("X_", "_X")));
    }

    @Test
    void rejectsUnknownSymbol() {
        assertThrows(IllegalArgumentException.class, () -> LevelParser.parse(Arrays.asList("X_", "_#")));
    }

    @Test
    void rejectsLinesOfDifferentLength() {
        assertThrows(IllegalArgumentException.class, () -> LevelParser.parse(Arrays.asList("X__", "_T")));
    }

    @Test
    void rejectsEmptyLevel() {
        assertThrows(IllegalArgumentException.class, () -> LevelParser.parse(Collections.emptyList()));
    }
}
