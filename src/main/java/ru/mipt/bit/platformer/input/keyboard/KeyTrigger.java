package ru.mipt.bit.platformer.input.keyboard;

import ru.mipt.bit.platformer.input.Trigger;

public class KeyTrigger implements Trigger {

    private final KeyboardState keyboard;
    private final int[] keycodes;

    public KeyTrigger(KeyboardState keyboard, int... keycodes) {
        if (keycodes.length == 0) {
            throw new IllegalArgumentException("Нужна хотя бы одна клавиша");
        }
        this.keyboard = keyboard;
        this.keycodes = keycodes.clone();
    }

    @Override
    public boolean isActive() {
        for (int keycode : keycodes) {
            if (keyboard.isKeyPressed(keycode)) {
                return true;
            }
        }
        return false;
    }
}
