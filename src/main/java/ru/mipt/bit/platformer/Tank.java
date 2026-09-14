package ru.mipt.bit.platformer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;

public class Tank implements Disposable {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;

    private GridPosition position;

    private GridPosition destination;

    private float movementProgress = 1f;
    private float angle = 0f;

    public Tank(String texturePath, GridPosition initialPosition) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
        this.position = initialPosition;
        this.destination = initialPosition;
    }

    public GridPosition getPosition() {
        return position;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public void move(Direction direction, Level level) {
        if (isMoving()) {
            return;
        }
        GridPosition candidate = position.move(direction);
        if (level.isFree(candidate)) {
            destination = candidate;
            movementProgress = 0f;
        }
        angle = direction.getAngle();
    }

    public void update(Level level, float deltaTime) {
        level.moveBetweenTiles(rectangle, position, destination, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            position = destination;
        }
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, angle);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}