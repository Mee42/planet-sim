package dev.mee42.commands;

import dev.mee42.db.Database;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class ResetCommand extends Command {
    public ResetCommand() {
        super(false, true, true, "Reset", "reset", "reset the database");
    }

    @Override
    public void run(Context context) {
        Database.inst.reset();
        context.createMessage("Reset");
    }
}
