package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.control.RandomTankController;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameWorldTest {

    private static final float DURATION = 0.4f;

    @Test
    void createsAllCommandsBeforeExecutingAnyOfThem() {
        GameLevel level = new GameLevel(10, 8);
        GameWorld world = new GameWorld(level);
        List<String> log = new ArrayList<>();
        world.addController(() -> {
            log.add("create 1");
            return Collections.singletonList((Command) () -> log.add("execute 1"));
        });
        world.addController(() -> {
            log.add("create 2");
            return Collections.singletonList((Command) () -> log.add("execute 2"));
        });

        world.update(0.1f);

        assertEquals(Arrays.asList("create 1", "create 2", "execute 1", "execute 2"), log);
    }

    @Test
    void objectsLiveAfterCommandsAreExecuted() {
        GameLevel level = new GameLevel(10, 8);
        GameWorld world = new GameWorld(level);
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        level.add(tank);
        world.addController(() -> Collections.singletonList(new MoveCommand(tank, Direction.RIGHT, level)));

        world.update(DURATION);

        assertEquals(new GridPosition(2, 1), tank.getPosition());
    }

    @Test
    void randomTanksNeverShareACellNorLeaveTheField() {
        int width = 6;
        int height = 5;
        Random random = new Random(3);
        GameLevel level = new GameLevel(width, height);
        GameWorld world = new GameWorld(level);
        for (int i = 0; i < 6; i++) {
            Tank tank = new Tank(new GridPosition(i, i % height), DURATION);
            level.add(tank);
            world.addController(new RandomTankController(tank, level, random));
        }

        for (int frame = 0; frame < 2000; frame++) {
            world.update(0.05f);

            Set<GridPosition> claimed = new HashSet<>();
            for (GameObject object : level.getObjects()) {
                if (!(object instanceof Tank)) {
                    continue;
                }
                Tank tank = (Tank) object;
                assertInside(tank.getPosition(), width, height);
                assertInside(tank.getDestination(), width, height);
                assertTrue(claimed.add(tank.getPosition()), "два танка в клетке " + tank.getPosition());
                if (!tank.getDestination().equals(tank.getPosition())) {
                    assertTrue(claimed.add(tank.getDestination()), "два танка целятся в " + tank.getDestination());
                }
            }
            for (GameObject object : level.getObjects()) {
                if (object instanceof Bullet) {
                    assertInside(((Bullet) object).getPosition(), width, height);
                }
            }
        }
    }

    private static void assertInside(GridPosition cell, int width, int height) {
        assertTrue(cell.getX() >= 0 && cell.getX() < width && cell.getY() >= 0 && cell.getY() < height,
                "объект вне поля: " + cell);
    }
}
