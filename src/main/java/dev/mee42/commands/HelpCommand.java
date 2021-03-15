package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;
import discord4j.rest.util.Color;

import java.util.Random;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(false, false, false, "help","get some help!", "use `ps help [name]` for more information. Like you just did.");
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
                    spec.addField(command.name, command.shortHelp, true);
                }
            }else {
                    spec.setTitle("THIS PART OF THE HELP MENU ISN'T DONE");
                    spec.setDescription("DON'T USE IT");
            }
        });
    }
}
