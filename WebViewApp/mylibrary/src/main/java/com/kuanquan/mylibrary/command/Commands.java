package com.kuanquan.mylibrary.command;

import com.kuanquan.mylibrary.interfaces.Command;

import java.util.HashMap;

public abstract class Commands {

    private HashMap<String, Command> commands;

    abstract int getCommandLevel();

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public Commands() {
        commands = new HashMap<>();
    }

    protected void registerCommand(Command command) {
        commands.put(command.name(), command);
    }
}


