package ru.mipt.bit.platformer.model;

public class Tank implements Obstacle {

    private final float movementDuration;

    private GridPosition position;
    private GridPosition destination;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;

    public Tank(GridPosition initialPosition, float movementDuration) {
        if (movementDuration <= 0f) {
            throw new IllegalArgumentException("Время переезда должно быть положительным: " + movementDuration);
        }
        this.position = initialPosition;
        this.destination = initialPosition;
        this.movementDuration = movementDuration;
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

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    @Override
    public boolean occupies(GridPosition cell) {
        return position.equals(cell) || destination.equals(cell);
    }

    public void move(Direction newDirection, ObstacleMap obstacles) {
        if (isMoving()) {
            return;
        }
        GridPosition candidate = position.move(newDirection);
        if (obstacles.isFree(candidate)) {
            destination = candidate;
            movementProgress = 0f;
        }
        direction = newDirection;
    }

    public void update(float deltaTime) {
        movementProgress = Math.min(1f, movementProgress + deltaTime / movementDuration);
        if (!isMoving()) {
            position = destination;
        }
    }
}
