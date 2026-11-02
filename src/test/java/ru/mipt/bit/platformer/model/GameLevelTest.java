package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameLevelTest {

    private static final class RecordingListener implements LevelListener {
        private final List<String> events = new ArrayList<>();

        @Override
        public void onObjectAdded(GameObject object) {
            events.add("+" + object);
        }

        @Override
        public void onObjectRemoved(GameObject object) {
            events.add("-" + object);
        }
    }

    private static final class CountingObject implements GameObject {
        private final GridPosition cell;
        private float lived;

        private CountingObject(GridPosition cell) {
            this.cell = cell;
        }

        @Override
        public boolean occupies(GridPosition other) {
            return cell.equals(other);
        }

        @Override
        public void live(float deltaTime) {
            lived += deltaTime;
        }

        @Override
        public String toString() {
            return "obj" + cell;
        }
    }

    @Test
    void notifiesListenersAboutAddedObjects() {
        GameLevel level = new GameLevel(5, 5);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        CountingObject object = new CountingObject(new GridPosition(1, 1));

        level.add(object);

        assertEquals(Arrays.asList("+obj(1, 1)"), listener.events);
        assertTrue(level.contains(object));
    }

    @Test
    void notifiesListenersAboutRemovedObjects() {
        GameLevel level = new GameLevel(5, 5);
        RecordingListener listener = new RecordingListener();
        CountingObject object = new CountingObject(new GridPosition(1, 1));
        level.add(object);
        level.addListener(listener);

        level.remove(object);

        assertEquals(Arrays.asList("-obj(1, 1)"), listener.events);
        assertFalse(level.contains(object));
    }

    @Test
    void notifiesEveryListener() {
        GameLevel level = new GameLevel(5, 5);
        RecordingListener first = new RecordingListener();
        RecordingListener second = new RecordingListener();
        level.addListener(first);
        level.addListener(second);

        level.add(new CountingObject(new GridPosition(0, 0)));

        assertEquals(1, first.events.size());
        assertEquals(1, second.events.size());
    }

    @Test
    void addingOrRemovingTwiceNotifiesOnce() {
        GameLevel level = new GameLevel(5, 5);
        RecordingListener listener = new RecordingListener();
        level.addListener(listener);
        CountingObject object = new CountingObject(new GridPosition(1, 1));

        level.add(object);
        level.add(object);
        level.remove(object);
        level.remove(object);

        assertEquals(2, listener.events.size());
    }

    @Test
    void tickMakesEveryObjectLive() {
        GameLevel level = new GameLevel(5, 5);
        CountingObject first = new CountingObject(new GridPosition(0, 0));
        CountingObject second = new CountingObject(new GridPosition(1, 0));
        level.add(first);
        level.add(second);

        level.tick(0.25f);

        assertEquals(0.25f, first.lived);
        assertEquals(0.25f, second.lived);
    }

    @Test
    void objectRemovedDuringTickDoesNotLive() {
        GameLevel level = new GameLevel(5, 5);
        CountingObject victim = new CountingObject(new GridPosition(1, 0));
        GameObject killer = new GameObject() {
            @Override
            public boolean occupies(GridPosition cell) {
                return false;
            }

            @Override
            public void live(float deltaTime) {
                level.remove(victim);
            }
        };
        level.add(killer);
        level.add(victim);

        level.tick(0.25f);

        assertEquals(0f, victim.lived);
    }

    @Test
    void cellIsFreeOnlyInsideAndWithoutObjects() {
        GameLevel level = new GameLevel(5, 5);
        level.add(new CountingObject(new GridPosition(2, 2)));

        assertTrue(level.isFree(new GridPosition(1, 1)));
        assertFalse(level.isFree(new GridPosition(2, 2)));
        assertFalse(level.isFree(new GridPosition(5, 1)));
        assertFalse(level.isFree(new GridPosition(-1, 1)));
    }

    @Test
    void findsObjectInCellExceptGivenOne() {
        GameLevel level = new GameLevel(5, 5);
        CountingObject object = new CountingObject(new GridPosition(2, 2));
        level.add(object);

        assertSame(object, level.objectAt(new GridPosition(2, 2), null).get());
        assertFalse(level.objectAt(new GridPosition(2, 2), object).isPresent());
        assertFalse(level.objectAt(new GridPosition(3, 3), null).isPresent());
    }

    @Test
    void anyNewKindOfObjectWorksWithoutChangingTheLevel() {
        GameLevel level = new GameLevel(10, 8);
        GameObject wall = new GameObject() {
            @Override
            public boolean occupies(GridPosition cell) {
                return cell.getY() == 3;
            }

            @Override
            public void live(float deltaTime) {
            }
        };
        level.add(wall);

        assertFalse(level.isFree(new GridPosition(7, 3)));
        assertTrue(level.isFree(new GridPosition(7, 4)));
    }

    @Test
    void reportsItsSize() {
        GameLevel level = new GameLevel(10, 8);

        assertEquals(10, level.getWidth());
        assertEquals(8, level.getHeight());
    }
}
