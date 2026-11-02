package ru.mipt.bit.platformer.model;

public class Tank implements GameObject {

    private static final int DEFAULT_HEALTH = 2;
    private static final BulletSettings DEFAULT_BULLET = new BulletSettings(1, 0.1f);
    private static final float DEFAULT_RELOAD_DURATION = 0.5f;

    private final float movementDuration;
    private final BulletSettings bulletSettings;
    private final float reloadDuration;

    private GridPosition position;
    private GridPosition destination;
    private float movementProgress = 1f;
    private Direction direction = Direction.RIGHT;
    private int health;
    private float reloadLeft = 0f;

    public Tank(GridPosition initialPosition, float movementDuration) {
        this(initialPosition, movementDuration, DEFAULT_HEALTH, DEFAULT_BULLET, DEFAULT_RELOAD_DURATION);
    }

    public Tank(GridPosition initialPosition, float movementDuration, int health,
                BulletSettings bulletSettings, float reloadDuration) {
        if (movementDuration <= 0f) {
            throw new IllegalArgumentException("Время переезда должно быть положительным: " + movementDuration);
        }
        if (health < 0) {
            throw new IllegalArgumentException("Жизни не могут быть отрицательными: " + health);
        }
        if (reloadDuration < 0f) {
            throw new IllegalArgumentException("Время перезарядки не может быть отрицательным: " + reloadDuration);
        }
        this.position = initialPosition;
        this.destination = initialPosition;
        this.movementDuration = movementDuration;
        this.health = health;
        this.bulletSettings = bulletSettings;
        this.reloadDuration = reloadDuration;
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

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return health < 0;
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public boolean isReloading() {
        return reloadLeft > 0f;
    }

    @Override
    public boolean occupies(GridPosition cell) {
        return position.equals(cell) || destination.equals(cell);
    }

    public void move(Direction newDirection, ObstacleMap obstacles) {
        if (isDestroyed() || isMoving()) {
            return;
        }
        GridPosition candidate = position.move(newDirection);
        if (obstacles.isFree(candidate)) {
            destination = candidate;
            movementProgress = 0f;
        }
        direction = newDirection;
    }

    public void shoot(GameLevel level) {
        if (isDestroyed() || isMoving() || isReloading()) {
            return;
        }
        reloadLeft = reloadDuration;
        GridPosition start = position.move(direction);
        if (level.isInside(start)) {
            level.add(new Bullet(level, start, direction, bulletSettings));
        }
    }

    @Override
    public void live(float deltaTime) {
        reloadLeft = Math.max(0f, reloadLeft - deltaTime);
        movementProgress = Math.min(1f, movementProgress + deltaTime / movementDuration);
        if (!isMoving()) {
            position = destination;
        }
    }

    @Override
    public void onHit(int damage, GameLevel level) {
        health -= damage;
        if (isDestroyed()) {
            level.remove(this);
        }
    }
}
