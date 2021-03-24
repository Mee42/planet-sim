package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import discord4j.rest.util.Color;

import java.time.Duration;
import java.time.Instant;

public class StatsCommand extends Command {
    public StatsCommand() {
        super(false, false, false, "stats", "Information about the bot", "Information about the bot");
    }

    public static final Instant startedAt = Instant.now();
    
    @Override
    public void run(Context context) {
        context.createEmbed(spec -> {
            spec.setColor(Color.LIGHT_SEA_GREEN);
            spec.setTitle("Planet Sim Bot Stats");
            spec.setFooter("Started at", null);
            spec.setTimestamp(startedAt);
            var duration = Duration.between(startedAt, Instant.now());
            spec.setDescription("Uptime: " + String.format("%d:%02d:%02d", duration.toHours(), duration.toMinutesPart(), duration.toSecondsPart()));
        });
    }
}
