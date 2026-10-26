package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.command.Command;

import java.util.List;

public interface Controller {
    List<Command> nextCommands();
}
