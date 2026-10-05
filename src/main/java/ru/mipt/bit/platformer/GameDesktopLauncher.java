package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.graphics.LevelRenderer;
import ru.mipt.bit.platformer.graphics.Scene;
import ru.mipt.bit.platformer.graphics.TankRenderer;
import ru.mipt.bit.platformer.graphics.TreeRenderer;
import ru.mipt.bit.platformer.input.InputHandler;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboardState;
import ru.mipt.bit.platformer.input.keyboard.KeyTrigger;
import ru.mipt.bit.platformer.input.keyboard.KeyboardState;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GridPosition;
import ru.mipt.bit.platformer.model.ObstacleMap;
import ru.mipt.bit.platformer.model.Obstacles;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_MAP = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final float TANK_MOVEMENT_DURATION = 0.4f;

    private Batch batch;
    private Scene scene;
    private Tank player;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();
        LevelRenderer level = new LevelRenderer(LEVEL_MAP, batch, Interpolation.smooth);
        scene = new Scene(batch, level);

        Obstacles obstacles = new Obstacles();

        player = new Tank(new GridPosition(1, 1), TANK_MOVEMENT_DURATION);
        obstacles.add(player);
        scene.add(new TankRenderer(TANK_TEXTURE, player, level));

        Tree tree = new Tree(new GridPosition(1, 3));
        obstacles.add(tree);
        scene.add(new TreeRenderer(TREE_TEXTURE, tree, level));

        inputHandler = createInputHandler(new GdxKeyboardState(), obstacles);
    }

    private InputHandler createInputHandler(KeyboardState keyboard, ObstacleMap obstacles) {
        InputHandler handler = new InputHandler();
        handler.bind(new KeyTrigger(keyboard, UP, W), () -> player.move(Direction.UP, obstacles));
        handler.bind(new KeyTrigger(keyboard, LEFT, A), () -> player.move(Direction.LEFT, obstacles));
        handler.bind(new KeyTrigger(keyboard, DOWN, S), () -> player.move(Direction.DOWN, obstacles));
        handler.bind(new KeyTrigger(keyboard, RIGHT, D), () -> player.move(Direction.RIGHT, obstacles));
        return handler;
    }

    @Override
    public void render() {
        inputHandler.update();
        player.update(Gdx.graphics.getDeltaTime());
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
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}
