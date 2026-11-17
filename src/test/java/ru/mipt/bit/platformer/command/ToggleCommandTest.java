package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ToggleCommandTest {

    @Test
    void togglesTargetOnEveryExecution() {
        int[] toggles = {0};
        ToggleCommand command = new ToggleCommand(() -> toggles[0]++);

        command.execute();
        command.execute();

        assertEquals(2, toggles[0]);
    }

    @Test
    void doesNothingUntilExecuted() {
        int[] toggles = {0};

        new ToggleCommand(() -> toggles[0]++);

        assertEquals(0, toggles[0]);
    }
}
