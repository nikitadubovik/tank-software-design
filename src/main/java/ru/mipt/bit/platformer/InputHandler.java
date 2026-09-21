package ru.mipt.bit.platformer;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    private final InputSource inputSource;
    private final List<KeyBinding> bindings = new ArrayList<>();

    public InputHandler(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    public void bind(int primaryKey, int secondaryKey, Runnable action) {
        bindings.add(new KeyBinding(primaryKey, secondaryKey, action));
    }

    public void update() {
        for (KeyBinding binding : bindings) {
            if (inputSource.isKeyPressed(binding.primaryKey) || inputSource.isKeyPressed(binding.secondaryKey)) {
                binding.action.run();
            }
        }
    }

    private static final class KeyBinding {
        private final int primaryKey;
        private final int secondaryKey;
        private final Runnable action;

        private KeyBinding(int primaryKey, int secondaryKey, Runnable action) {
            this.primaryKey = primaryKey;
            this.secondaryKey = secondaryKey;
            this.action = action;
        }
    }
}