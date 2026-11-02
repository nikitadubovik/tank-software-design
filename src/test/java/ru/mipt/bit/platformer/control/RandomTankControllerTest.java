package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTankControllerTest {

    @Test
    void givesOneCommandToStandingTank() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController controller = new RandomTankController(tank, level, new Random(1));

        List<Command> commands = controller.nextCommands();

        assertEquals(1, commands.size());
    }

    @Test
    void givesNothingWhileTankIsMoving() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        tank.move(Direction.UP, level);
        RandomTankController controller = new RandomTankController(tank, level, new Random(1));

        assertTrue(controller.nextCommands().isEmpty());
    }

    @Test
    void eventuallyUsesEveryDirectionAndShoots() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController controller = new RandomTankController(tank, level, new Random(7));
        Set<Direction> directions = EnumSet.noneOf(Direction.class);
        int shots = 0;

        for (int i = 0; i < 200; i++) {
            Command command = controller.nextCommands().get(0);
            if (command instanceof MoveCommand) {
                directions.add(((MoveCommand) command).getDirection());
            } else if (command instanceof ShootCommand) {
                shots++;
            }
        }

        assertEquals(EnumSet.allOf(Direction.class), directions);
        assertTrue(shots > 0);
    }

    @Test
    void sameSeedGivesSameChoices() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        RandomTankController first = new RandomTankController(tank, level, new Random(42));
        RandomTankController second = new RandomTankController(tank, level, new Random(42));

        for (int i = 0; i < 20; i++) {
            assertEquals(describe(first.nextCommands().get(0)), describe(second.nextCommands().get(0)));
        }
    }

    private static String describe(Command command) {
        if (command instanceof MoveCommand) {
            return "move " + ((MoveCommand) command).getDirection();
        }
        return command.getClass().getSimpleName();
    }
}
