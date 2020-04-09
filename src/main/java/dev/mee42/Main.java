package dev.mee42;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;
import dev.mee42.discord.HelpCommand;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Optional;

public class Main {
  public static void main(String[] args) {
    System.err.println("Planet Sim!\n");
    System.err.flush();

    // add all the commands
    Discord.commands.add(new HelpCommand());

    Discord.client.getEventDispatcher().on(MessageCreateEvent.class)
            .flatMap(m -> {
              if(m.getMessage().getContent().orElse("").equals("ps mine")) {
                return m.getMessage().getChannel().flatMap(c -> c.createMessage("sorry, there was an error mining for you. You may have mined before this hour"));
              } else {
                return Mono.empty();
              }
            }).subscribe();


    Discord.client.getEventDispatcher().on(MessageCreateEvent.class)
            .filter(it -> it.getMessage().getContent().isPresent())
            .filter(it -> it.getMessage().getContent().get().startsWith("ps"))
            .subscribe(it -> {
                System.out.println(it.getMessage().getContent().get());
                String[] split =  it.getMessage().getContent().get().substring(2).trim().split(" ", 2);
                System.out.println(Arrays.toString(split));
                if(split.length == 0 || split[0].trim().isEmpty()) {
                    return;
                }
                String commandStr = split[0];
                String arguments = split.length == 2 ? split[1] : "";
                Optional<Command> command = Discord.commands.stream().filter(c -> c.name.equalsIgnoreCase(commandStr.trim())).findFirst();
                if(command.isEmpty()) {
                    System.out.println("couldn't find command");
                    return; // deal with later
                }
                Context context = new Context(it.getMessage(), arguments);
                command.get().run(context);
            });

    Discord.client.login().block();
  }
}
