package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTankController implements Controller {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Tank tank;
    private final ObstacleMap obstacles;
    private final Random random;

    public RandomTankController(Tank tank, ObstacleMap obstacles, Random random) {
        this.tank = tank;
        this.obstacles = obstacles;
        this.random = random;
    }

    @Override
    public List<Command> nextCommands() {
        if (tank.isMoving()) {
            return Collections.emptyList();
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return Collections.singletonList(new MoveCommand(tank, direction, obstacles));
    }
}
