package dev.mee42.discord;


import dev.mee42.db.DatabaseObject;
import dev.mee42.db.Player;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.util.Snowflake;


// This class is immutable
public class Context {
    final Message message;
    final String argument;
    final String messageContent;
    final Snowflake authorID;
    final DatabaseObject<Player> player;


    public Context(Message message, String argument) {
        this.message = message;
        this.argument = argument;
        this.messageContent = message.getContent().orElse("");
        authorID = message.getAuthor().map(User::getId).orElseThrow(() -> new RuntimeException("oof"));
        this.player = Player.getByDiscordID(authorID);
    }
}


