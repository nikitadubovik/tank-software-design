package ru.mipt.bit.platformer.model;

public interface GameObject extends Obstacle {

    void live(float deltaTime);

    default void onHit(int damage, GameLevel level) {
    }
}
