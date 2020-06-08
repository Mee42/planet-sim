package dev.mee42.db;

import discord4j.core.object.util.Snowflake;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Player {
    public final Snowflake discordID;
    public final int id;
    public final String name;
    public Location location;

    public Player(Snowflake discordID, int id, String name, Location location) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(discordID);
        this.discordID = discordID;
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static Player getByDiscordID(Snowflake authorID) {
        return all.stream().filter(i -> i.discordID.equals(authorID)).findFirst().orElse(null);
    }
    private static final List<Player> all = new ArrayList<>();
    public static List<Player> getAll() {
        return all;
    }
    public static Player getById(int id){ return all.stream().filter(it -> it.id == id).findFirst().orElse(null); }
    @Override
    public String toString() {
        return "Player{" +
                "discordID=" + discordID +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
