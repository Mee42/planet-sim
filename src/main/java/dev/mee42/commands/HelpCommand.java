package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import dev.mee42.discord.Discord;

import java.awt.*;
import java.util.Random;

public class HelpCommand extends Command {
    public HelpCommand() {
        super(false, false, false, "help","get some help!", "use `ps help [name]` for more information. Like you just did.");
    }

    @Override
    public void run(Context context) {
        context.message.getChannel().flatMap(c -> c.createEmbed(spec -> {
            spec.setColor(Color.getHSBColor(new Random().nextFloat() / 1, 0.7f, 0.7f));
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
        })).block();
    }
}
