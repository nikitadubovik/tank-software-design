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

    @Override
    public void create() {
        batch = new SpriteBatch();

        level = new Level(LEVEL_MAP, batch);
        level.addObstacle(TREE_TEXTURE, new GridPosition(1, 3));

        player = new Tank(TANK_TEXTURE, new GridPosition(1, 1));
    }

    @Override
    public void render() {
        clearScreen();

        handlePlayerInput();
        player.update(level, Gdx.graphics.getDeltaTime());

        level.render();

        batch.begin();
        player.render(batch);
        level.renderObstacles(batch);
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void handlePlayerInput() {
        if (isPressed(UP, W)) {
            player.move(Direction.UP, level);
        }
        if (isPressed(LEFT, A)) {
            player.move(Direction.LEFT, level);
        }
        if (isPressed(DOWN, S)) {
            player.move(Direction.DOWN, level);
        }
        if (isPressed(RIGHT, D)) {
            player.move(Direction.RIGHT, level);
        }
    }

    private boolean isPressed(int arrowKey, int letterKey) {
        return Gdx.input.isKeyPressed(arrowKey) || Gdx.input.isKeyPressed(letterKey);
    }

    @Override
    public void resize(int width, int height) {
        // окно не масштабируется
    }

    @Override
    public void pause() {
        // игра не ставится на паузу
    }

    @Override
    public void resume() {
        // игра не ставится на паузу
    }

    @Override
    public void dispose() {
        player.dispose();
        level.dispose();
        batch.dispose();
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // уровень 10 клеток x 128px в ширину, 8 x 128px в высоту
        config.setWindowedMode(1280, 1024);
        new Lwjgl3Application(new GameDesktopLauncher(), config);
    }
}