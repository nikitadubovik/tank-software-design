package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.control.Controller;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private final List<Tank> tanks = new ArrayList<>();
    private final List<Controller> controllers = new ArrayList<>();

    public void addTank(Tank tank) {
        tanks.add(tank);
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public void update(float deltaTime) {
        for (Controller controller : controllers) {
            for (Command command : controller.nextCommands()) {
                command.execute();
            }
        }
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }
    }
}
