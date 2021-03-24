package dev.mee42.commands;

import dev.mee42.db.Database;
import dev.mee42.db.Player;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

import java.time.Duration;
import java.time.Instant;

public class MineCommand extends Command {
    public MineCommand() {
        super(false, false, false, "mine", "Mines some resources", "Mines some. Make sure you're in the right channel");
    }
    

    @Override
    public void run(Context context) {
        var existing = Database.inst.jdbi.withHandle(h ->
                h.createQuery("SELECT * FROM users WHERE id = :id LIMIT 1")
                        .bind("id", context.authorID)
                        .mapTo(Player.class).findFirst()
        );
        if(existing.isEmpty())  {
            context.createMessage("You don't seem to be in the system yet");
            return;
        }
        var player = existing.get();
        Duration duration = Duration.between(Instant.now(), player.lastMine).abs();



        if(duration.compareTo(Duration.ofMinutes(1)) < 1) {
            context.createMessage("You've already mined this minute! You have " + (60 - duration.getSeconds()) + " seconds left");
            return;
        }
        int newMines = player.mines + 1;
        Database.inst.jdbi.useHandle(h -> {
            h.execute("UPDATE users SET mines = ?, lastMine = ? WHERE id = ?", newMines, Instant.now(), context.authorID);
        });
        context.createMessage("Mined! You've now mined " + newMines + " times");
    }
}
