package ru.mipt.bit.platformer.input.keyboard;

import com.badlogic.gdx.Gdx;

public class GdxKeyboardState implements KeyboardState {

    @Override
    public boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }
}
