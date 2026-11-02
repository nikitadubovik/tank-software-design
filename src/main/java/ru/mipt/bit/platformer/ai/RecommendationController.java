package ru.mipt.bit.platformer.ai;

import org.awesome.ai.AI;
import org.awesome.ai.Action;
import org.awesome.ai.Recommendation;
import org.awesome.ai.state.GameState;
import org.awesome.ai.state.immovable.Obstacle;
import org.awesome.ai.state.movable.Bot;
import org.awesome.ai.state.movable.Orientation;
import org.awesome.ai.state.movable.Player;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.control.Controller;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationController implements Controller {

    private final AI ai;
    private final GameLevel level;
    private final Tank player;

    public RecommendationController(AI ai, GameLevel level, Tank player) {
        this.ai = ai;
        this.level = level;
        this.player = player;
    }

    @Override
    public List<Command> nextCommands() {
        if (!level.contains(player)) {
            return Collections.emptyList();
        }
        List<Command> commands = new ArrayList<>();
        for (Recommendation recommendation : ai.recommend(toGameState())) {
            Object source = recommendation.getActor().getSource();
            if (source instanceof Tank && source != player && level.contains((Tank) source)) {
                commands.add(toCommand((Tank) source, recommendation.getAction()));
            }
        }
        return commands;
    }

    GameState toGameState() {
        List<Obstacle> obstacles = new ArrayList<>();
        List<Bot> bots = new ArrayList<>();
        for (GameObject object : level.getObjects()) {
            if (object instanceof Tree) {
                Tree tree = (Tree) object;
                obstacles.add(new Obstacle(tree.getPosition().getX(), tree.getPosition().getY()));
            } else if (object instanceof Tank && object != player) {
                bots.add(toBot((Tank) object));
            }
        }
        return GameState.builder()
                .player(toPlayer(player))
                .bots(bots)
                .obstacles(obstacles)
                .levelWidth(level.getWidth())
                .levelHeight(level.getHeight())
                .build();
    }

    private static Player toPlayer(Tank tank) {
        return Player.builder()
                .source(tank)
                .x(tank.getPosition().getX())
                .y(tank.getPosition().getY())
                .destX(tank.getDestination().getX())
                .destY(tank.getDestination().getY())
                .orientation(toOrientation(tank.getDirection()))
                .build();
    }

    private static Bot toBot(Tank tank) {
        return Bot.builder()
                .source(tank)
                .x(tank.getPosition().getX())
                .y(tank.getPosition().getY())
                .destX(tank.getDestination().getX())
                .destY(tank.getDestination().getY())
                .orientation(toOrientation(tank.getDirection()))
                .build();
    }

    static Orientation toOrientation(Direction direction) {
        switch (direction) {
            case UP:
                return Orientation.N;
            case LEFT:
                return Orientation.W;
            case DOWN:
                return Orientation.S;
            case RIGHT:
                return Orientation.E;
            default:
                throw new IllegalArgumentException("Неизвестное направление: " + direction);
        }
    }

    private Command toCommand(Tank tank, Action action) {
        switch (action) {
            case Shoot:
                return new ShootCommand(tank, level);
            case MoveNorth:
                return new MoveCommand(tank, Direction.UP, level);
            case MoveWest:
                return new MoveCommand(tank, Direction.LEFT, level);
            case MoveSouth:
                return new MoveCommand(tank, Direction.DOWN, level);
            case MoveEast:
                return new MoveCommand(tank, Direction.RIGHT, level);
            default:
                throw new IllegalArgumentException("Неизвестное действие: " + action);
        }
    }
}
