package dev.mee42.commands;

import dev.mee42.Util;
import dev.mee42.db.*;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import discord4j.core.spec.EmbedCreateSpec;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StatsCommand extends Command {

    public StatsCommand() {
        super(false, false, false, "stats", "show information about ",
                "show generic info, or show stats about \"players\", \"planets\", \"solarSystems\", \"moons\", \"transits\", \"storageBodies\"");
    }

    private void genericInfo(EmbedCreateSpec spec) {
        spec.setTitle("generic info");

        String locations = Util.fold(Location.allLocations
                .stream()
                .map(Location::toNiceString)
                .collect(toList()), (a,b) -> a + "\n" + b).orElse("<no locations exist>");
        spec.addField("All Locations", locations, false);

        String players = Util.fold(Player.getAll()
                .stream()
                .map(player -> player.name + "(id: " + player.id + ") is at " + player.location.toNiceString())
                .collect(toList()), (a,b) -> a + "\n" + b).orElse("<no players exist>");

        spec.addField("Players", players, false);

        String solarSystems = Util.fold(SolarSystem.getAll()
                .stream()
                .map(ss -> ss.name + "(id: " + ss.id + ") is at ( " + ss.xPos + "," + ss.yPos + ")")
                .collect(toList()), (a,b) -> a + "\n" + b).orElse("<no solar systems exist>");

        spec.addField("Solar Systems", solarSystems, false);

    }

    private void playersInfo(EmbedCreateSpec spec) {
        spec.setTitle("players info");
        Player.getAll().forEach(p -> {
            Location location = p.location;
            String locationString = location instanceof Transit ? "In " : "At ";
            spec.addField(p.name + " (" + p.id + ", " + p.discordID.asString() + ")", locationString + location.toNiceString(), false);
        });
    }
    private void planetsInfo(EmbedCreateSpec spec) {
        Planet.getAll()
                .forEach(planet -> spec.addField(planet.name + "(id: " + planet.id + ")",
                        "in solar system: " + planet.solarSystem.name + "\n owned by:" +
                                (planet.ownedBy == null ? "no one" : planet.ownedBy.name), false)
                );
    }



    private void solarSystemsInfo(EmbedCreateSpec spec) {
        spec.setTitle("solar systems info TODO");
    }
    private void moonsInfo(EmbedCreateSpec spec) {
        spec.setTitle("moons info TODO");
    }
    private void transitInfo(EmbedCreateSpec spec) {
        spec.setTitle("transits info TODO");
    }
    private final Instant startedAt = Instant.now();

    private final String developer = Util.getPasswords().get(2);

    private void botInfo(EmbedCreateSpec spec) {
        spec.setTitle("Bot Info");
        var d = Duration.between(startedAt, Instant.now());
        spec.setDescription("uptime: " + d.toDaysPart() + "d "+  d.toHoursPart() + "h " + d.toMinutesPart() + "m " + d.toSecondsPart() + "s " + d.toMillisPart() + "ms");
        spec.setFooter("Started at", null);
        spec.addField("Machine Running:", developer, false);
        spec.setTimestamp(startedAt);
    }

    private void withId(EmbedCreateSpec spec, int id) {
        Location l = Location.byId(id);
        if(l != null) {
            if(l instanceof Planet) {
                Planet planet = (Planet)l;
                spec.setTitle(planet.name + " (Planet, id: " + planet.id + ")");
                spec.setDescription("" +
                        "Owned By: " + (planet.ownedBy==null?"No one":planet.ownedBy.name) +
                        "\nIs in solar system: " + planet.solarSystem.name);
                Stream<String> moons = Moon.getAll()
                        .filter(it -> it.planet == planet)
                        .map(it -> it.name + " (id: " + it.id + ")");
                spec.addField("Moons", Util.fold(moons).orElse("<No Moons>"), false);
            } else if(l instanceof Moon) {
                Moon moon = (Moon)l;
                spec.setTitle(moon.name + " (Moon, id: " + moon.id + ")");
                String owned = (moon.ownedBy == null) ? "" : "\nOwned By: " + moon.ownedBy.name;
                spec.setDescription("orbits around " + moon.planet.toNiceString() + owned);
            } else if(l instanceof Transit) {
                spec.setTitle("Transit not supported ATM. TODO will be fixed");
            }
            return;
        }
        SolarSystem ss = SolarSystem.getById(id);
        if(ss != null){
            spec.setTitle(ss.name + " (Solar System, id: " + ss.id+ ")");
            spec.setDescription("Position:  (" + ss.xPos + ", " + ss.yPos + ")");
            Stream<String> planets = Planet.getAll()
                    .filter(it -> it.solarSystem == ss)
                    .map(it -> it.name + " (id: " + it.id + ")");
            spec.addField("Planets", Util.fold(planets).orElse("<No Planets>"), false);
            return;
        }
        Player player = Player.getById(id);
        if(player != null){
            spec.setTitle(player.name + " (id: " + player.id + ")");
            spec.setDescription("is at: " + player.location.toNiceString());
            spec.addField("Discord ID", player.discordID.asString(), false);
            return;
        }
        spec.setTitle("Can't lookup that type of resource yet, or there does not exist a resource with that id");
        spec.setColor(Color.RED);

    }

    @Override
    public void run(Context context) {
        if(context.argument.isEmpty()) {
            // generic info
            context.createEmbed(this::genericInfo).block();
        } else if(context.argument.equalsIgnoreCase("players") || context.argument.equalsIgnoreCase("player")){
            context.createEmbed(this::playersInfo).block();
        } else if(context.argument.equalsIgnoreCase("planets") || context.argument.equalsIgnoreCase("planet")){
            context.createEmbed(this::planetsInfo).block();
    } else if (Stream.of("solar systems","solarsystems", "solarsystem","solar system").anyMatch(s -> s.equalsIgnoreCase(context.argument))){
            context.createEmbed(this::solarSystemsInfo).block();
        } else if(context.argument.equalsIgnoreCase("moon") || context.argument.equalsIgnoreCase("moons")){
            context.createEmbed(this::moonsInfo).block();
        } else if(context.argument.equalsIgnoreCase("transits") || context.argument.equalsIgnoreCase("transit")){
            context.createEmbed(this::transitInfo).block();
        } else if(context.argument.equalsIgnoreCase("bot")){
            context.createEmbed(this::botInfo).block();
        } else {
            // try to parse it as an integer
            try {
                int id = Integer.parseInt(context.argument.trim());
                context.createEmbed(spec -> withId(spec, id)).block();
            } catch(NumberFormatException e) {
                context.createEmbed(s -> {
                    s.setTitle("Valid stats options: players, planets, solar systems, moon, transits, bot, or any game ID.");
                }).block();
            }
        }
    }

}
