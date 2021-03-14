package dev.mee42.commands;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import dev.mee42.db.Database;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import org.bson.Document;

import java.time.Duration;
import java.time.Instant;

public class MineCommand extends Command {
    public MineCommand() {
        super(false, false, false, "Mine", "mine", "Mines some. Make sure you're in the right channel");
    }
    

    @Override
    public void run(Context context) {
        long id = context.authorID.asLong();
        Document existing = Database.inst.db.getCollection("users").find(Filters.eq("_id", id)).first();
        Duration duration = Duration.between(Instant.now(), Instant.ofEpochMilli(existing.get("last_mine", Long.class))).abs();
        if(duration.compareTo(Duration.ofMinutes(1)) < 1) {
            context.createMessage("You've already mined this minute! You have " + (60 - duration.getSeconds()) + " seconds left").block();
            return;
        }
        int newMines = existing.getInteger("mines") + 1;
        Database.inst.db.getCollection("users").updateOne(Filters.eq("_id", id), Updates.combine(Updates.set("mines", newMines), Updates.set("last_mine", Instant.now().toEpochMilli())));
        context.createMessage("you have mined " + newMines + " times, <@" + id + ">!").block();
    }
}
