package dev.mee42.commands;

import dev.mee42.Util;
import dev.mee42.db.Saver;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class SaveCommand extends Command {
    public SaveCommand() {
        super(false, false, true, "save", "", "");
    }

    @Override
    public void run(Context context) {
        Util.makeUnchecked(() -> {
            Saver.save();
            return true;
        });
        context.createMessage("Saved").block();
    }
}
