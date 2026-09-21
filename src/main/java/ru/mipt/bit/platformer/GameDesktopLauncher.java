package ru.mipt.bit.platformer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_MAP = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private Batch batch;
    private Level level;
    private Tank player;
    private TankRenderer playerRenderer;
    private InputHandler inputHandler;

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new Level(LEVEL_MAP, batch);
        level.addObstacle(TREE_TEXTURE, new GridPosition(1, 3));

        player = new Tank(new GridPosition(1, 1));
        playerRenderer = new TankRenderer(TANK_TEXTURE);

        inputHandler = new InputHandler(new GdxInputSource());
        inputHandler.bind(UP, W, () -> player.move(Direction.UP, level.getObstacles()));
        inputHandler.bind(LEFT, A, () -> player.move(Direction.LEFT, level.getObstacles()));
        inputHandler.bind(DOWN, S, () -> player.move(Direction.DOWN, level.getObstacles()));
        inputHandler.bind(RIGHT, D, () -> player.move(Direction.RIGHT, level.getObstacles()));
        // сюда добавится будущий обработчик, например:
        // inputHandler.bind(SPACE, SPACE, player::shoot);
    }

    @Override
    public void render() {
        clearScreen();

        inputHandler.update();
        player.update(Gdx.graphics.getDeltaTime());
        playerRenderer.update(level, player);

        level.render();

        batch.begin();
        playerRenderer.render(batch, player);
        level.renderObstacles(batch);
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
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
        playerRenderer.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}