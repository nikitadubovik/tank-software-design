package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTankController implements Controller {

    private static final Direction[] DIRECTIONS = Direction.values();

    private final Tank tank;
    private final GameLevel level;
    private final Random random;

    public RandomTankController(Tank tank, GameLevel level, Random random) {
        this.tank = tank;
        this.level = level;
        this.random = random;
    }

    @Override
    public List<Command> nextCommands() {
        if (tank.isMoving()) {
            return Collections.emptyList();
        }
        int choice = random.nextInt(DIRECTIONS.length + 1);
        if (choice == DIRECTIONS.length) {
            return Collections.singletonList(new ShootCommand(tank, level));
        }
        return Collections.singletonList(new MoveCommand(tank, DIRECTIONS[choice], level));
    }
}
