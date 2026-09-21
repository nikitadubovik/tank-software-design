package ru.mipt.bit.platformer;

import static com.badlogic.gdx.math.MathUtils.clamp;
import static com.badlogic.gdx.math.MathUtils.isEqual;

public class Tank {

    private static final float MOVEMENT_SPEED = 0.4f;

    private GridPosition position;
    private GridPosition destination;
    private float movementProgress = 1f;
    private float angle = 0f;

    public Tank(GridPosition initialPosition) {
        this.position = initialPosition;
        this.destination = initialPosition;
    }

    public GridPosition getPosition() {
        return position;
    }

    public GridPosition getDestination() {
        return destination;
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public float getAngle() {
        return angle;
    }

    public boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }

    public void move(Direction direction, ObstacleMap obstacles) {
        if (isMoving()) {
            return;
        }
        GridPosition candidate = position.move(direction);
        if (obstacles.isFree(candidate)) {
            destination = candidate;
            movementProgress = 0f;
        }
        angle = direction.getAngle();
    }

    public void update(float deltaTime) {
        movementProgress = clamp(movementProgress + deltaTime / MOVEMENT_SPEED, 0f, 1f);
        if (!isMoving()) {
            position = destination;
        }
    }
}