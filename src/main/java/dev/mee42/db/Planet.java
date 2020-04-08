package dev.mee42.db;

public class Planet extends Location {
    public final String name;
    public final DatabaseObject<SolarSystem> solarSystem;
    public final DatabaseObject<Player> ownedBy; // can be null

    Planet(int id, String name, DatabaseObject<SolarSystem> solarSystem, DatabaseObject<Player> ownedBy) {
        super(id);
        this.name = name;
        this.solarSystem = solarSystem;
        this.ownedBy = ownedBy;
    }

    public static DatabaseObject<Planet> getPlanet(int id){
        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("SELECT * FROM planet WHERE id = :id")
                        .bind("id", id)
                        .map((rs, ctx) -> {
                            int ownedBy = rs.getInt("ownedBy");
                            return new Planet(rs.getInt("id"),
                                           rs.getString("name"),
                                           SolarSystem.get(rs.getInt("solarSystem")),
                                           Player.get(ownedBy));
                        })
                        .list()
        ).stream().findFirst().orElse(null));
    }

    @Override
    public String toString() {
        return "Planet{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", solarSystem=" + solarSystem +
                ", ownedBy=" + ownedBy +
                '}';
    }
}
