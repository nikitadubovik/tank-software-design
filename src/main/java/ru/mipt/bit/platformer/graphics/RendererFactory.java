package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RendererFactory {

    private final Map<Class<?>, Function<GameObject, Renderable>> creators = new HashMap<>();

    public <T extends GameObject> void register(Class<T> type, Function<T, Renderable> creator) {
        creators.put(type, object -> creator.apply(type.cast(object)));
    }

    public Renderable create(GameObject object) {
        Function<GameObject, Renderable> creator = creators.get(object.getClass());
        if (creator == null) {
            throw new IllegalArgumentException("Не знаю, как нарисовать " + object.getClass().getSimpleName());
        }
        return creator.apply(object);
    }
}
