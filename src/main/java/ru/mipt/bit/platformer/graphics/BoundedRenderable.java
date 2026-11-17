package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.Rectangle;

public interface BoundedRenderable extends Renderable {
    Rectangle getBounds();
}
