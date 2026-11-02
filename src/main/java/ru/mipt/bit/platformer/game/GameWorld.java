package ru.mipt.bit.platformer.game;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.control.Controller;
import ru.mipt.bit.platformer.model.GameLevel;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private final GameLevel level;
    private final List<Controller> controllers = new ArrayList<>();

    public GameWorld(GameLevel level) {
        this.level = level;
    }

    public void addController(Controller controller) {
        controllers.add(controller);
    }

    public void update(float deltaTime) {
        List<Command> commands = new ArrayList<>();
        for (Controller controller : controllers) {
            commands.addAll(controller.nextCommands());
        }
        for (Command command : commands) {
            command.execute();
        }
        level.tick(deltaTime);
    }
}
