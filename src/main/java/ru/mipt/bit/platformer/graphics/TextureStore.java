package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class TextureStore implements Disposable {

    private final Map<String, Texture> textures = new HashMap<>();

    public Texture get(String path) {
        return textures.computeIfAbsent(path, Texture::new);
    }

    @Override
    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
