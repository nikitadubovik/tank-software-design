package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    @Test
    void occupiesOnlyItsOwnCell() {
        Tree tree = new Tree(new GridPosition(1, 3));

        assertTrue(tree.occupies(new GridPosition(1, 3)));
        assertFalse(tree.occupies(new GridPosition(1, 2)));
    }

    @Test
    void keepsItsPosition() {
        assertEquals(new GridPosition(1, 3), new Tree(new GridPosition(1, 3)).getPosition());
    }
}
