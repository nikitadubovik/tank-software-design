package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Scene implements Disposable {

    private final Batch batch;
    private final LevelRenderer level;
    private final List<Renderable> renderables = new ArrayList<>();

    public Scene(Batch batch, LevelRenderer level) {
        this.batch = batch;
        this.level = level;
    }

    public void add(Renderable renderable) {
        renderables.add(renderable);
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        level.render();

        batch.begin();
        for (Renderable renderable : renderables) {
            renderable.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (Renderable renderable : renderables) {
            renderable.dispose();
        }
        level.dispose();
    }
}
