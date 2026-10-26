package ru.mipt.bit.platformer.command;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements Command {

    private final Tank tank;
    private final Direction direction;
    private final ObstacleMap obstacles;

    public MoveCommand(Tank tank, Direction direction, ObstacleMap obstacles) {
        this.tank = tank;
        this.direction = direction;
        this.obstacles = obstacles;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void execute() {
        tank.move(direction, obstacles);
    }
}
