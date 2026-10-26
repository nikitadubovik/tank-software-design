package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.command.Command;
import ru.mipt.bit.platformer.control.Controller;

import java.util.ArrayList;
import java.util.List;

public class InputHandler implements Controller {

    private final List<Binding> bindings = new ArrayList<>();

    public void bind(Trigger trigger, Command command) {
        bindings.add(new Binding(trigger, command));
    }

    @Override
    public List<Command> nextCommands() {
        List<Command> commands = new ArrayList<>();
        for (Binding binding : bindings) {
            if (binding.trigger.isActive()) {
                commands.add(binding.command);
            }
        }
        return commands;
    }

    private static final class Binding {
        private final Trigger trigger;
        private final Command command;

        private Binding(Trigger trigger, Command command) {
            this.trigger = trigger;
            this.command = command;
        }
    }
}
