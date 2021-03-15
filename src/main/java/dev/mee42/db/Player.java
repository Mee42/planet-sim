package dev.mee42.db;

import discord4j.common.util.Snowflake;

import java.time.Instant;

public class Player {
    public final Snowflake id;
    public final String name;
    public final int mines;
    public final Instant lastMine;

    public Player(Snowflake id, String name, int mines, Instant lastMine) {
        this.id = id;
        this.name = name;
        this.mines = mines;
        this.lastMine = lastMine;
    }
}
