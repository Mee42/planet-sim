package dev.mee42.db;

import discord4j.core.object.util.Snowflake;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.statement.Query;


interface Querier {
    Query query(Handle handle);
}

public class Player {
    public final long discordID;
    public final int id;
    public final String name;
    public final DatabaseObject<Location> location;

    public static DatabaseObject<Player> get(int id){

        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("")
                        .bind("id", id)
                        .map((rs, ctx) -> new Player(rs.getLong("discordID"),
                                rs.getInt("id"),
                                rs.getString("name"),
                                Location.get(rs.getInt("location"))))
                        .list()
        ).stream().findFirst().orElse(null));
    }

    private static DatabaseObject<Player> getFromQuery(Querier query) {
        return new DatabaseObject<Player>((p) -> p.id, () -> Database.connection.withHandle((handle) ->
                query.query(handle)
                        .map((rs, ctx) -> new Player(rs.getLong("discordID"),
                                rs.getInt("id"),
                                rs.getString("name"),
                                Location.get(rs.getInt("location"))))
                        .list()
        ).stream().findFirst().orElse(null));
    }

    public Player(long discordID, int id, String name, DatabaseObject<Location> location) {
        this.discordID = discordID;
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public static DatabaseObject<Player> getByDiscordID(Snowflake authorID) {
        return null;
    }

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
