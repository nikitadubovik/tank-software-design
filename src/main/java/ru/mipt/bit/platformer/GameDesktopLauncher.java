package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import org.awesome.ai.strategy.NotRecommendingAI;
import ru.mipt.bit.platformer.ai.RecommendationController;
import ru.mipt.bit.platformer.command.MoveCommand;
import ru.mipt.bit.platformer.command.ShootCommand;
import ru.mipt.bit.platformer.control.RandomTankController;
import ru.mipt.bit.platformer.game.GameWorld;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.RendererFactory;
import ru.mipt.bit.platformer.graphics.Scene;
import ru.mipt.bit.platformer.input.InputHandler;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboardState;
import ru.mipt.bit.platformer.input.keyboard.KeyTrigger;
import ru.mipt.bit.platformer.input.keyboard.KeyboardState;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.LevelLayout;
import ru.mipt.bit.platformer.level.RandomEnemyTanksGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameLevel;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_MAP = "level.tmx";
    private static final float TANK_MOVEMENT_DURATION = 0.4f;
    private static final String LEVEL_FILE = "level.txt";
    private static final String RANDOM_LEVEL_ARGUMENT = "random";
    private static final int FIELD_WIDTH = 10;
    private static final int FIELD_HEIGHT = 8;
    private static final int RANDOM_TREE_COUNT = 12;
    private static final int ENEMY_TANK_COUNT = 4;

    private final LevelGenerator levelGenerator;
    private final Random random;

    private Batch batch;
    private Scene scene;
    private GameWorld world;

    public GameDesktopLauncher(LevelGenerator levelGenerator, Random random) {
        this.levelGenerator = levelGenerator;
        this.random = random;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        LevelLayout layout = levelGenerator.generate();
        GameLevel gameLevel = new GameLevel(layout.getWidth(), layout.getHeight());
        Tank player = new Tank(layout.getPlayerStart(), TANK_MOVEMENT_DURATION);

        LevelRenderer levelRenderer = new LevelRenderer(LEVEL_MAP, batch, Interpolation.smooth);
        scene = new Scene(batch, levelRenderer, new RendererFactory(levelRenderer, player));
        gameLevel.addListener(scene);

        world = new GameWorld(gameLevel);

        for (GridPosition treePosition : layout.getTreePositions()) {
            gameLevel.add(new Tree(treePosition));
        }

        gameLevel.add(player);
        world.addController(createPlayerInput(new GdxKeyboardState(), player, gameLevel));

        for (GridPosition enemyPosition : layout.getEnemyTankPositions()) {
            Tank enemy = new Tank(enemyPosition, TANK_MOVEMENT_DURATION);
            gameLevel.add(enemy);
            world.addController(new RandomTankController(enemy, gameLevel, random));
        }

        world.addController(new RecommendationController(new NotRecommendingAI(), gameLevel, player));
    }

    private static InputHandler createPlayerInput(KeyboardState keyboard, Tank player, GameLevel level) {
        InputHandler handler = new InputHandler();
        handler.bind(new KeyTrigger(keyboard, UP, W), new MoveCommand(player, Direction.UP, level));
        handler.bind(new KeyTrigger(keyboard, LEFT, A), new MoveCommand(player, Direction.LEFT, level));
        handler.bind(new KeyTrigger(keyboard, DOWN, S), new MoveCommand(player, Direction.DOWN, level));
        handler.bind(new KeyTrigger(keyboard, RIGHT, D), new MoveCommand(player, Direction.RIGHT, level));
        handler.bind(new KeyTrigger(keyboard, SPACE), new ShootCommand(player, level));
        return handler;
    }

    @Override
    public void render() {
        world.update(Gdx.graphics.getDeltaTime());
        scene.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        scene.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        Random random = new Random();
        LevelGenerator levelGenerator = new RandomEnemyTanksGenerator(chooseLevelGenerator(args, random), ENEMY_TANK_COUNT, random);
        new Lwjgl3Application(new GameDesktopLauncher(levelGenerator, random), config);
    }

    private static LevelGenerator chooseLevelGenerator(String[] args, Random random) {
        if (args.length > 0 && RANDOM_LEVEL_ARGUMENT.equals(args[0])) {
            return new RandomLevelGenerator(FIELD_WIDTH, FIELD_HEIGHT, RANDOM_TREE_COUNT, random);
        }
        return new FileLevelGenerator(LEVEL_FILE);
    }
}
