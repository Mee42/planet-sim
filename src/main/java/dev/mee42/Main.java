package dev.mee42;

import dev.mee42.commands.*;
import dev.mee42.db.*;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;

import java.util.Optional;


public class Main {

    public static void main(String[] args) {
        System.out.println("Planet Sim!\n");
        System.err.flush();

        Database db = Database.inst;// make sure constructor runs

        // add all the commands
        Discord.commands.add(new HelpCommand());
        Discord.commands.add(new InfoCommand());
        Discord.commands.add(new RegisterCommand());
        Discord.commands.add(new CrashCommand());
        Discord.commands.add(new ResetCommand());
        Discord.commands.add(new MineCommand());
        Discord.commands.add(new StatsCommand());

        Discord.gateway.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(it -> it.getMessage().getChannelId().equals(Snowflake.of(697597664845365279L)) &&
                        it.getMessage().getContent().startsWith("ps"))
                .subscribe(Main::onMessage);

        Discord.gateway.getEventDispatcher()
                .on(ReadyEvent.class)
                .flatMap(y -> Discord.gateway.getChannelById(Snowflake.of(697597664845365279L)))
                .cast(MessageChannel.class)
                .flatMap(c -> c.createMessage("Started up. *(Got ready event)* - " + Util.getSecrets().get(2)))
                .subscribe();


        Discord.gateway.onDisconnect().block();
    }


    private static void onMessage(MessageCreateEvent message) {
        String[] split = message.getMessage().getContent().substring(2).trim().split(" ", 2);
        if (split.length == 0 || split[0].trim().isEmpty()) {
            return;
        }
        String commandStr = split[0];
        String arguments = split.length == 2 ? split[1] : "";
        Optional<Command> command = Discord.commands.stream().filter(c -> c.name.equalsIgnoreCase(commandStr.trim())).findFirst();
        Context context = new Context(message.getMessage(), arguments.trim());
        if (command.isEmpty()) {
            context.createMessage("can't find command \"" + commandStr + "\"");
            return; // deal with later
        }
        Command c = command.get();
        System.out.println("Running command " + c.name + " author: " + context.authorID + "  message: " + context.messageContent);
        try {
            c.run(context);
        } catch (UserScrewedUpException e) {
            context.createEmbed(spec -> {
                spec.setColor(Color.RED);
                spec.setTitle("User Error: ");
                spec.setDescription(e.getMessage());
            });
        } catch (Throwable e) {
            e.printStackTrace();
            context.createEmbed(spec -> {
                spec.setColor(Color.RED);
                spec.setTitle("Runtime Error: " + e.getClass().getCanonicalName());
                spec.setDescription(e.getMessage());
            });
        }
    }
}
