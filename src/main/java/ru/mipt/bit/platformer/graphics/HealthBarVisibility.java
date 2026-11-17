package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.command.Toggleable;

public class HealthBarVisibility implements Toggleable {

    private boolean visible;

    public HealthBarVisibility(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void toggle() {
        visible = !visible;
    }
}
