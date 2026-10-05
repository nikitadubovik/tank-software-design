package ru.mipt.bit.platformer.input;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {

    private final List<Binding> bindings = new ArrayList<>();

    public void bind(Trigger trigger, Runnable action) {
        bindings.add(new Binding(trigger, action));
    }

    public void update() {
        for (Binding binding : bindings) {
            if (binding.trigger.isActive()) {
                binding.action.run();
            }
        }
    }

    private static final class Binding {
        private final Trigger trigger;
        private final Runnable action;

        private Binding(Trigger trigger, Runnable action) {
            this.trigger = trigger;
            this.action = action;
        }
    }
}
