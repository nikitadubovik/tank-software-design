package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OnActivationTriggerTest {

    private boolean pressed;
    private final OnActivationTrigger trigger = new OnActivationTrigger(() -> pressed);

    @Test
    void inactiveWhileNotPressed() {
        assertFalse(trigger.isActive());
    }

    @Test
    void activeOnlyOnFirstFrameOfPress() {
        pressed = true;

        assertTrue(trigger.isActive());
        assertFalse(trigger.isActive());
        assertFalse(trigger.isActive());
    }

    @Test
    void activeAgainAfterRelease() {
        pressed = true;
        trigger.isActive();
        pressed = false;
        trigger.isActive();
        pressed = true;

        assertTrue(trigger.isActive());
    }
}
