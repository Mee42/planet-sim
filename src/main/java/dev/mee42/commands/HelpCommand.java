package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;
import discord4j.rest.util.Color;

import java.util.Random;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(false, false, false, "help","Get some help!", "use `ps help [name]` for more information. Like you just did.");
    }

    @Override
    public void run(Context context) {
        context.createEmbed(spec -> {
            var color = java.awt.Color.getHSBColor(new Random().nextFloat(), 0.7f, 0.7f);
            spec.setColor(Color.of(color.getRed(), color.getGreen(), color.getBlue()));
            if(context.argument.trim().isEmpty()) {
                spec.setTitle("Planet Sim Help");
                spec.setDescription("do `ps help [name] for more information");
                for(Command command : Discord.commands) {
                    spec.addField(command.name, command.shortHelp, false);
                }
            } else {
                var command = Discord.commands.stream().filter(it -> it.name.equals(context.argument.trim())).findFirst();
                if(command.isEmpty()) {
                    spec.setTitle("I can't find that command");
                    spec.setColor(Color.RED);
                } else {
                    spec.setTitle("Help for ps " + context.argument.trim());
                    spec.setDescription(command.get().longHelp);
                }
            }
        });
    }
}
