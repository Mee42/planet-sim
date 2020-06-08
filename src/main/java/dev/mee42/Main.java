package dev.mee42;

import dev.mee42.commands.*;
import dev.mee42.db.*;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.MessageChannel;
import discord4j.core.object.util.Snowflake;

import java.awt.*;
import java.util.Optional;

public class Main {

    static void reset() {
        SolarSystem.getAll().clear();
        SolarSystem solarSystem = new SolarSystem(10, "main-solar-system", 0, 0);
        SolarSystem.getAll().add(solarSystem);
        Location.allLocations.clear();
        Planet earth = new Planet("Earth", solarSystem, null, 11);
        Location.allLocations.add(earth);
        Moon theMoon = new Moon(12, "The Moon", earth, null);
        Location.allLocations.add(theMoon);
    }

    public static void main(String[] args) {
        System.out.println("Planet Sim!\n");
        System.err.flush();

        Util.makeUnchecked(Saver::load);

        // add all the commands
        Discord.commands.add(new HelpCommand());
        Discord.commands.add(new StatsCommand());
        Discord.commands.add(new RegisterCommand());
        Discord.commands.add(new DevCommand());
        Discord.commands.add(new SaveCommand());
        Discord.commands.add(new LoadCommand());


        Discord.client.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(it -> it.getMessage().getContent().isPresent() &&
                        it.getMessage().getChannelId().equals(Snowflake.of(697597664845365279L)) &&
                        it.getMessage().getContent().get().startsWith("ps"))
                .subscribe(it -> {
                    //noinspection ig toOptionalGetWithoutIsPresent
                    String[] split = it.getMessage().getContent().get().substring(2).trim().split(" ", 2);
                    if (split.length == 0 || split[0].trim().isEmpty()) {
                        return;
                    }
                    String commandStr = split[0];
                    String arguments = split.length == 2 ? split[1] : "";
                    Optional<Command> command = Discord.commands.stream().filter(c -> c.name.equalsIgnoreCase(commandStr.trim())).findFirst();
                    Context context = new Context(it.getMessage(), arguments);
                    if (command.isEmpty()) {
                        context.createMessage("can't find command \"" + commandStr + "\"").block();
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
                        }).block();
                    } catch (Throwable e) {
                        e.printStackTrace();
                        context.createEmbed(spec -> {
                            spec.setColor(Color.RED);
                            spec.setTitle("Runtime Error: " + e.getClass().getCanonicalName());
                            spec.setDescription(e.getMessage());
                        }).block();
                    }
                });

        Discord.client.getEventDispatcher()
                .on(ReadyEvent.class)
                .flatMap(x -> Discord.client.getChannelById(Snowflake.of(697597664845365279L)))
                .cast(MessageChannel.class)
                .flatMap(c -> c.createMessage("Started up. *(Got ready event)*"))
                .subscribe();
        Discord.client.login().block();
    }
}
