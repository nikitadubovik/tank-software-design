package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class Scene implements LevelListener, Disposable {

    private final Batch batch;
    private final LevelRenderer level;
    private final RendererFactory factory;
    private final Map<GameObject, Renderable> renderables = new LinkedHashMap<>();

    public Scene(Batch batch, LevelRenderer level, RendererFactory factory) {
        this.batch = batch;
        this.level = level;
        this.factory = factory;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        renderables.put(object, factory.create(object));
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        renderables.remove(object);
    }

    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);

        level.render();

        batch.begin();
        for (Renderable renderable : renderables.values()) {
            renderable.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        renderables.clear();
        factory.dispose();
        level.dispose();
    }
}
