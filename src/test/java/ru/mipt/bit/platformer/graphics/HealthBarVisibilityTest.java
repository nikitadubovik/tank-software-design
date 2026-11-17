package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.ToggleCommand;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthBarVisibilityTest {

    @Test
    void keepsInitialState() {
        assertFalse(new HealthBarVisibility(false).isVisible());
        assertTrue(new HealthBarVisibility(true).isVisible());
    }

    @Test
    void toggleSwitchesBackAndForth() {
        HealthBarVisibility visibility = new HealthBarVisibility(false);

        visibility.toggle();
        assertTrue(visibility.isVisible());

        visibility.toggle();
        assertFalse(visibility.isVisible());
    }

    @Test
    void toggleCommandSwitchesVisibility() {
        HealthBarVisibility visibility = new HealthBarVisibility(false);

        new ToggleCommand(visibility).execute();

        assertTrue(visibility.isVisible());
    }
}
