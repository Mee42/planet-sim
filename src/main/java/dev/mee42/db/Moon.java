package dev.mee42.db;

public class Moon extends Location {
    public final String name;
    public final DatabaseObject<Planet> planet;

    protected Moon(int id, String name, DatabaseObject<Planet> planet) {
        super(id);
        this.name = name;
        this.planet = planet;
    }
    public static DatabaseObject<Moon> getMoon(int id) {
        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("SELECT * FROM moon WHERE id = :id")
                        .bind("id", id)
                        .map((rs, ctx) -> new Moon(rs.getInt("id"), rs.getString("name"), Planet.getPlanet(rs.getInt("planet"))))
                        .list()
        ).stream().findFirst().orElse(null));
    }

    @Override
    public String toString() {
        return "Moon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet=" + planet +
                '}';
    }
}
