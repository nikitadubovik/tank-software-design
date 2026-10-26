package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputHandlerTest {

    private static final Command FIRST = () -> { };
    private static final Command SECOND = () -> { };

    @Test
    void activeTriggerGivesItsCommand() {
        InputHandler handler = new InputHandler();
        handler.bind(() -> true, FIRST);

        assertEquals(Collections.singletonList(FIRST), handler.nextCommands());
    }

    @Test
    void inactiveTriggerGivesNothing() {
        InputHandler handler = new InputHandler();
        handler.bind(() -> false, FIRST);

        assertTrue(handler.nextCommands().isEmpty());
    }

    @Test
    void onlyActiveBindingsAreReturned() {
        InputHandler handler = new InputHandler();
        handler.bind(() -> true, FIRST);
        handler.bind(() -> false, SECOND);

        assertEquals(Collections.singletonList(FIRST), handler.nextCommands());
    }

    @Test
    void commandsKeepBindingOrder() {
        InputHandler handler = new InputHandler();
        handler.bind(() -> true, FIRST);
        handler.bind(() -> true, SECOND);

        assertEquals(Arrays.asList(FIRST, SECOND), handler.nextCommands());
    }

    @Test
    void doesNotExecuteCommandsItself() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bind(() -> true, () -> calls[0]++);

        handler.nextCommands();

        assertEquals(0, calls[0]);
    }
}
