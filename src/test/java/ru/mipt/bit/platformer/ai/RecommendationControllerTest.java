package ru.mipt.bit.platformer.ai;

import org.awesome.ai.Action;
import org.awesome.ai.Recommendation;
import org.awesome.ai.state.GameState;
import org.awesome.ai.state.movable.Bot;
import org.awesome.ai.state.movable.Orientation;
import org.awesome.ai.strategy.NotRecommendingAI;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationControllerTest {

    private final GameLevel level = new GameLevel(10, 8);
    private final Tank player = new Tank(new GridPosition(1, 1), 0.4f);
    private final Tank enemy = new Tank(new GridPosition(5, 5), 0.4f);

    {
        level.add(new Tree(new GridPosition(3, 3)));
        level.add(player);
        level.add(enemy);
    }

    private static long bullets(GameLevel level) {
        return level.getObjects().stream().filter(o -> o instanceof Bullet).count();
    }

    @Test
    void describesLevelInTermsOfTheEngine() {
        GameState state = new RecommendationController(new NotRecommendingAI(), level, player).toGameState();

        assertEquals(10, state.getLevelWidth());
        assertEquals(8, state.getLevelHeight());
        assertSame(player, state.getPlayer().getSource());
        assertEquals(1, state.getPlayer().getX());
        assertEquals(1, state.getBots().size());
        assertSame(enemy, state.getBots().get(0).getSource());
        assertEquals(5, state.getBots().get(0).getY());
        assertEquals(1, state.getObstacles().size());
        assertEquals(3, state.getObstacles().get(0).getX());
    }

    @Test
    void passesMovementAndOrientation() {
        enemy.move(Direction.UP, level);

        Bot bot = new RecommendationController(new NotRecommendingAI(), level, player).toGameState().getBots().get(0);

        assertEquals(5, bot.getDestX());
        assertEquals(6, bot.getDestY());
        assertEquals(Orientation.N, bot.getOrientation());
        assertTrue(bot.isMoving());
    }

    @Test
    void mapsEveryDirectionToOrientation() {
        assertEquals(Orientation.N, RecommendationController.toOrientation(Direction.UP));
        assertEquals(Orientation.W, RecommendationController.toOrientation(Direction.LEFT));
        assertEquals(Orientation.S, RecommendationController.toOrientation(Direction.DOWN));
        assertEquals(Orientation.E, RecommendationController.toOrientation(Direction.RIGHT));
    }

    @Test
    void shootRecommendationMakesBotShoot() {
        RecommendationController controller = new RecommendationController(
                state -> recommend(state, Action.Shoot), level, player);

        for (Command command : controller.nextCommands()) {
            command.execute();
        }

        assertEquals(1, bullets(level));
    }

    @Test
    void moveRecommendationMovesBot() {
        RecommendationController controller = new RecommendationController(
                state -> recommend(state, Action.MoveWest), level, player);

        for (Command command : controller.nextCommands()) {
            command.execute();
        }

        assertEquals(new GridPosition(4, 5), enemy.getDestination());
    }

    @Test
    void ignoresRecommendationsForThePlayer() {
        RecommendationController controller = new RecommendationController(
                state -> Collections.singletonList(new Recommendation(state.getPlayer(), Action.MoveNorth)), level, player);

        assertTrue(controller.nextCommands().isEmpty());
    }

    @Test
    void ignoresRecommendationsForDestroyedTanks() {
        RecommendationController controller = new RecommendationController(
                state -> recommend(state, Action.Shoot), level, player);
        GameState state = controller.toGameState();
        level.remove(enemy);

        RecommendationController stale = new RecommendationController(
                ignored -> recommend(state, Action.Shoot), level, player);

        assertTrue(stale.nextCommands().isEmpty());
    }

    @Test
    void givesNothingWhenEngineRecommendsNothing() {
        assertTrue(new RecommendationController(new NotRecommendingAI(), level, player).nextCommands().isEmpty());
    }

    private static List<Recommendation> recommend(GameState state, Action action) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (Bot bot : state.getBots()) {
            recommendations.add(new Recommendation(bot, action));
        }
        return recommendations;
    }
}
