package ru.mipt.bit.platformer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameLevel implements ObstacleMap {

    private final int width;
    private final int height;
    private final FieldBounds bounds;
    private final Set<GameObject> objects = new LinkedHashSet<>();
    private final List<LevelListener> listeners = new ArrayList<>();

    public GameLevel(int width, int height) {
        this.width = width;
        this.height = height;
        this.bounds = new FieldBounds(width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addListener(LevelListener listener) {
        listeners.add(listener);
    }

    public void add(GameObject object) {
        if (objects.add(object)) {
            for (LevelListener listener : new ArrayList<>(listeners)) {
                listener.onObjectAdded(object);
            }
        }
    }

    public void remove(GameObject object) {
        if (objects.remove(object)) {
            for (LevelListener listener : new ArrayList<>(listeners)) {
                listener.onObjectRemoved(object);
            }
        }
    }

    public boolean contains(GameObject object) {
        return objects.contains(object);
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(new ArrayList<>(objects));
    }

    public boolean isInside(GridPosition cell) {
        return !bounds.occupies(cell);
    }

    public Optional<GameObject> objectAt(GridPosition cell, GameObject except) {
        for (GameObject object : objects) {
            if (object != except && object.occupies(cell)) {
                return Optional.of(object);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isFree(GridPosition cell) {
        return isInside(cell) && !objectAt(cell, null).isPresent();
    }

    public void tick(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (objects.contains(object)) {
                object.live(deltaTime);
            }
        }
    }
}
