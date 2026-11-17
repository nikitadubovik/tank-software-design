package ru.mipt.bit.platformer.command;

public class ToggleCommand implements Command {

    private final Toggleable target;

    public ToggleCommand(Toggleable target) {
        this.target = target;
    }

    @Override
    public void execute() {
        target.toggle();
    }
}
