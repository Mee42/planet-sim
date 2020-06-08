package dev.mee42.commands;

import dev.mee42.Util;
import dev.mee42.db.Saver;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class LoadCommand extends Command {
    public LoadCommand() {
        super(false, false, true, "load", "", "");
    }

    @Override
    public void run(Context context) {
        Util.makeUnchecked(() -> {
            Saver.load();
            return true;
        });
        context.createMessage("Loaded").block();
    }
}
