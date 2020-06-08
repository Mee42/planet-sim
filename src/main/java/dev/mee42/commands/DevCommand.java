package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class DevCommand extends Command {
    public DevCommand() {
        super(false, false, false, "Dev", "dev", "throws an exception");
    }

    @Override
    public void run(Context context) {
        throw new RuntimeException("test");
    }
}
