package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputHandlerTest {

    @Test
    void activeTriggerRunsAction() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bind(() -> true, () -> calls[0]++);

        handler.update();

        assertEquals(1, calls[0]);
    }

    @Test
    void inactiveTriggerDoesNotRunAction() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bind(() -> false, () -> calls[0]++);

        handler.update();

        assertEquals(0, calls[0]);
    }

    @Test
    void bindingsWorkIndependently() {
        InputHandler handler = new InputHandler();
        int[] first = {0};
        int[] second = {0};
        handler.bind(() -> true, () -> first[0]++);
        handler.bind(() -> false, () -> second[0]++);

        handler.update();

        assertEquals(1, first[0]);
        assertEquals(0, second[0]);
    }

    @Test
    void actionRepeatsEveryUpdateWhileTriggerIsActive() {
        InputHandler handler = new InputHandler();
        int[] calls = {0};
        handler.bind(() -> true, () -> calls[0]++);

        handler.update();
        handler.update();
        handler.update();

        assertEquals(3, calls[0]);
    }
}
