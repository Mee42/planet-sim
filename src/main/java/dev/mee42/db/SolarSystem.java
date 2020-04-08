package dev.mee42.db;

public class SolarSystem {
    public final int id;
    public final String name;
    public final int xPos;
    public final int yPos;

    @Override
    public String toString() {
        return "SolarSystem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", xPos=" + xPos +
                ", yPos=" + yPos +
                '}';
    }

    SolarSystem(int id, String name, int xPos, int yPos) {
        this.id = id;
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public static DatabaseObject<SolarSystem> get(int id){
        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("SELECT * FROM solarSystem WHERE id = :id")
                        .bind("id", id)
                        .map((rs, ctx) -> {
                            return new SolarSystem(rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getInt("xPos"),
                                    rs.getInt("yPos"));
                        })
                        .list()
        ).stream().findFirst().orElse(null));
    }
}
