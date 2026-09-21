package ru.mipt.bit.platformer;

import com.badlogic.gdx.Gdx;

public class GdxInputSource implements InputSource {

    @Override
    public boolean isKeyPressed(int keycode) {
        return Gdx.input.isKeyPressed(keycode);
    }
}