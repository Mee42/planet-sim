package dev.mee42.commands;

import dev.mee42.db.Database;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class InfoCommand extends Command {

    public InfoCommand() {
        super(false, false, false, "info", "Shows info about game data", "Shows how many times you've mined");
    }

    @Override
    public void run(Context context) {
        var mined = Database.inst.jdbi.withHandle(h ->
                h.createQuery("SELECT mines FROM users WHERE id = :id LIMIT 1").bind("id", context.authorID)
                    .mapTo(Integer.class).findFirst()
        );
        
        if(mined.isEmpty()) {
            context.createMessage("You don't appear to be in the system, hmm");
        } else {
            context.createMessage("You've mined " + mined.get() + " times, " + context.author.getMention() + "!");
        }
    }

}
