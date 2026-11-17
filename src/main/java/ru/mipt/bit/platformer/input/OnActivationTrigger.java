package ru.mipt.bit.platformer.input;

public class OnActivationTrigger implements Trigger {

    private final Trigger trigger;
    private boolean wasActive;

    public OnActivationTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public boolean isActive() {
        boolean active = trigger.isActive();
        boolean justActivated = active && !wasActive;
        wasActive = active;
        return justActivated;
    }
}
