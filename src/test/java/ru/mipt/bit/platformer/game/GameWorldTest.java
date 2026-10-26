package ru.mipt.bit.platformer.game;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.control.RandomTankController;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.FieldBounds;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Obstacles;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
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
    void executesCommandsOfEveryController() {
        GameWorld world = new GameWorld();
        int[] calls = {0};
        Command count = () -> calls[0]++;
        world.addController(() -> Collections.singletonList(count));
        world.addController(() -> Collections.singletonList(count));

        world.update(0.1f);

        assertEquals(2, calls[0]);
    }

    @Test
    void updatesEveryTank() {
        GameWorld world = new GameWorld();
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        world.addTank(tank);
        tank.move(Direction.UP, cell -> true);

        world.update(DURATION);

        assertEquals(new GridPosition(1, 2), tank.getPosition());
    }

    @Test
    void commandsAreExecutedBeforeTanksMove() {
        GameWorld world = new GameWorld();
        Tank tank = new Tank(new GridPosition(1, 1), DURATION);
        world.addTank(tank);
        world.addController(() -> Collections.singletonList(new MoveCommand(tank, Direction.RIGHT, cell -> true)));

        world.update(DURATION);

        assertEquals(new GridPosition(2, 1), tank.getPosition());
    }

    @Test
    void randomTanksNeverShareACellNorLeaveTheField() {
        int width = 6;
        int height = 5;
        Random random = new Random(3);
        GameWorld world = new GameWorld();
        Obstacles obstacles = new Obstacles();
        obstacles.add(new FieldBounds(width, height));
        List<Tank> tanks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Tank tank = new Tank(new GridPosition(i, i % height), DURATION);
            tanks.add(tank);
            obstacles.add(tank);
            world.addTank(tank);
            world.addController(new RandomTankController(tank, obstacles, random));
        }

        for (int frame = 0; frame < 2000; frame++) {
            world.update(0.05f);

            Set<GridPosition> claimed = new HashSet<>();
            for (Tank tank : tanks) {
                assertInside(tank.getPosition(), width, height);
                assertInside(tank.getDestination(), width, height);
                assertTrue(claimed.add(tank.getPosition()), "две машины в клетке " + tank.getPosition());
                if (!tank.getDestination().equals(tank.getPosition())) {
                    assertTrue(claimed.add(tank.getDestination()), "две машины целятся в " + tank.getDestination());
                }
            }
        }
    }

    private static void assertInside(GridPosition cell, int width, int height) {
        assertTrue(cell.getX() >= 0 && cell.getX() < width && cell.getY() >= 0 && cell.getY() < height,
                "танк вне поля: " + cell);
    }
}
