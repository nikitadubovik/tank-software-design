package ru.mipt.bit.platformer.input.keyboard;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyTriggerTest {

    private static final int ARROW_UP = 1;
    private static final int LETTER_W = 2;
    private static final int OTHER_KEY = 3;

    private final Set<Integer> pressed = new HashSet<>();
    private final KeyboardState keyboard = pressed::contains;

    @Test
    void activeWhenFirstKeyIsPressed() {
        pressed.add(ARROW_UP);
        assertTrue(new KeyTrigger(keyboard, ARROW_UP, LETTER_W).isActive());
    }

    @Test
    void activeWhenAnyOtherListedKeyIsPressed() {
        pressed.add(LETTER_W);
        assertTrue(new KeyTrigger(keyboard, ARROW_UP, LETTER_W).isActive());
    }

    @Test
    void inactiveWhenNothingIsPressed() {
        assertFalse(new KeyTrigger(keyboard, ARROW_UP, LETTER_W).isActive());
    }

    @Test
    void inactiveWhenOnlyUnlistedKeyIsPressed() {
        pressed.add(OTHER_KEY);
        assertFalse(new KeyTrigger(keyboard, ARROW_UP, LETTER_W).isActive());
    }

    @Test
    void acceptsSingleKey() {
        pressed.add(OTHER_KEY);
        assertTrue(new KeyTrigger(keyboard, OTHER_KEY).isActive());
    }

    @Test
    void requiresAtLeastOneKey() {
        assertThrows(IllegalArgumentException.class, () -> new KeyTrigger(keyboard));
    }
}
