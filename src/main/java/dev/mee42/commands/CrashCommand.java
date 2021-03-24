package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class CrashCommand extends Command {
    public CrashCommand() {
        super(false, false, false, "crash", "Crashes the bot", "throws an exception");
    }

    @Override
    public void run(Context context) {
        throw new RuntimeException(":ohno:");
    }
}