package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Tank;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankControllerTest {

    private static final ObstacleMap EVERYTHING_FREE = cell -> true;

    @Test
    void givesOneMoveCommandToStandingTank() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController controller = new RandomTankController(tank, EVERYTHING_FREE, new Random(1));

        List<Command> commands = controller.nextCommands();

        assertEquals(1, commands.size());
        assertTrue(commands.get(0) instanceof MoveCommand);
    }

    @Test
    void givesNothingWhileTankIsMoving() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        tank.move(Direction.UP, EVERYTHING_FREE);
        RandomTankController controller = new RandomTankController(tank, EVERYTHING_FREE, new Random(1));

        assertTrue(controller.nextCommands().isEmpty());
    }

    @Test
    void commandStartsMovement() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController controller = new RandomTankController(tank, EVERYTHING_FREE, new Random(1));

        controller.nextCommands().get(0).execute();

        assertTrue(tank.isMoving());
    }

    @Test
    void eventuallyUsesEveryDirection() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController controller = new RandomTankController(tank, EVERYTHING_FREE, new Random(7));
        Set<Direction> seen = EnumSet.noneOf(Direction.class);

        for (int i = 0; i < 100; i++) {
            seen.add(((MoveCommand) controller.nextCommands().get(0)).getDirection());
        }

        assertEquals(EnumSet.allOf(Direction.class), seen);
    }

    @Test
    void sameSeedGivesSameChoices() {
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController first = new RandomTankController(tank, EVERYTHING_FREE, new Random(42));
        RandomTankController second = new RandomTankController(tank, EVERYTHING_FREE, new Random(42));

        for (int i = 0; i < 20; i++) {
            Direction a = ((MoveCommand) first.nextCommands().get(0)).getDirection();
            Direction b = ((MoveCommand) second.nextCommands().get(0)).getDirection();
            assertEquals(a, b);
        }
    }
}
