package ru.mipt.bit.platformer.level;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.GridPosition;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileLevelGeneratorTest {

    @Test
    void readsLevelFromResourceFile() {
        LevelLayout layout = new FileLevelGenerator("test-level.txt").generate();

        assertEquals(3, layout.getWidth());
        assertEquals(3, layout.getHeight());
        assertEquals(new GridPosition(1, 1), layout.getPlayerStart());
        assertEquals(Arrays.asList(new GridPosition(0, 2), new GridPosition(2, 0)), layout.getTreePositions());
    }

    @Test
    void failsOnMissingFile() {
        assertThrows(IllegalArgumentException.class, () -> new FileLevelGenerator("no-such-level.txt").generate());
    }
}
