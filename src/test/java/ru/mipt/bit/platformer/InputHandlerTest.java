package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputHandlerTest {

    private static final class FakeInputSource implements InputSource {
        private final Set<Integer> pressedKeys = new HashSet<>();

        void press(int keycode) {
            pressedKeys.add(keycode);
        }

        @Override
        public boolean isKeyPressed(int keycode) {
            return pressedKeys.contains(keycode);
        }
    }

    private static final int UP_KEY = 1;
    private static final int W_KEY = 2;
    private static final int UNUSED_KEY = 99;

    @Test
    void pressingPrimaryKeyTriggersAction() {
        FakeInputSource input = new FakeInputSource();
        InputHandler handler = new InputHandler(input);
        boolean[] triggered = {false};
        handler.bind(UP_KEY, W_KEY, () -> triggered[0] = true);

        input.press(UP_KEY);
        handler.update();

        assertTrue(triggered[0]);
    }

    @Test
    void pressingSecondaryKeyAlsoTriggersAction() {
        FakeInputSource input = new FakeInputSource();
        InputHandler handler = new InputHandler(input);
        boolean[] triggered = {false};
        handler.bind(UP_KEY, W_KEY, () -> triggered[0] = true);

        input.press(W_KEY);
        handler.update();

        assertTrue(triggered[0]);
    }

    @Test
    void unboundKeyDoesNotTriggerAction() {
        FakeInputSource input = new FakeInputSource();
        InputHandler handler = new InputHandler(input);
        boolean[] triggered = {false};
        handler.bind(UP_KEY, W_KEY, () -> triggered[0] = true);

        input.press(UNUSED_KEY);
        handler.update();

        assertFalse(triggered[0]);
    }

    @Test
    void severalBindingsWorkIndependently() {
        FakeInputSource input = new FakeInputSource();
        InputHandler handler = new InputHandler(input);
        boolean[] upTriggered = {false};
        boolean[] downTriggered = {false};
        handler.bind(UP_KEY, W_KEY, () -> upTriggered[0] = true);
        handler.bind(UNUSED_KEY, UNUSED_KEY, () -> downTriggered[0] = true);

        input.press(UP_KEY);
        handler.update();

        assertTrue(upTriggered[0]);
        assertFalse(downTriggered[0]);
    }
}