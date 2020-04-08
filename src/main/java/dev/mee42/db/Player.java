package dev.mee42.db;

public class Player {
    public final long discordID;
    public final int id;
    public final String name;
    public final DatabaseObject<Location> location;

    public static DatabaseObject<Player> get(int id){
        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("SELECT * FROM player WHERE id = :id")
                        .bind("id", id)
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
