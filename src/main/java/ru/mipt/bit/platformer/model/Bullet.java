package ru.mipt.bit.platformer.model;

import java.util.Optional;

public class Bullet implements GameObject {

    private final GameLevel level;
    private final Direction direction;
    private final BulletSettings settings;

    private GridPosition position;
    private GridPosition destination;
    private float movementProgress = 1f;

    public Bullet(GameLevel level, GridPosition start, Direction direction, BulletSettings settings) {
        this.level = level;
        this.direction = direction;
        this.settings = settings;
        this.position = start;
        this.destination = start;
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

    public int getDamage() {
        return settings.getDamage();
    }

    @Override
    public boolean occupies(GridPosition cell) {
        return position.equals(cell) || destination.equals(cell);
    }

    @Override
    public void live(float deltaTime) {
        Optional<GameObject> collision = objectInOwnCells();
        if (collision.isPresent()) {
            hit(collision.get());
            return;
        }

        movementProgress = Math.min(1f, movementProgress + deltaTime / settings.getCellDuration());
        if (movementProgress < 1f) {
            return;
        }

        position = destination;
        GridPosition next = position.move(direction);
        if (!level.isInside(next)) {
            level.remove(this);
            return;
        }
        Optional<GameObject> target = level.objectAt(next, this);
        if (target.isPresent()) {
            hit(target.get());
            return;
        }
        destination = next;
        movementProgress = 0f;
    }

    @Override
    public void onHit(int damage, GameLevel level) {
        level.remove(this);
    }

    private Optional<GameObject> objectInOwnCells() {
        Optional<GameObject> found = level.objectAt(position, this);
        if (!found.isPresent() && !destination.equals(position)) {
            found = level.objectAt(destination, this);
        }
        return found;
    }

    private void hit(GameObject target) {
        level.remove(this);
        target.onHit(settings.getDamage(), level);
    }
}
