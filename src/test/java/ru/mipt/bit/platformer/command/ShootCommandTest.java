package ru.mipt.bit.platformer.command;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShootCommandTest {

    private static long bullets(GameLevel level) {
        return level.getObjects().stream().filter(o -> o instanceof Bullet).count();
    }

    @Test
    void doesNothingUntilExecuted() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        level.add(tank);

        new ShootCommand(tank, level);

        assertEquals(0, bullets(level));
    }

    @Test
    void makesTankShootWhenExecuted() {
        GameLevel level = new GameLevel(10, 8);
        Tank tank = new Tank(new GridPosition(1, 1), 0.4f);
        level.add(tank);

        new ShootCommand(tank, level).execute();

        assertEquals(1, bullets(level));
    }
}
