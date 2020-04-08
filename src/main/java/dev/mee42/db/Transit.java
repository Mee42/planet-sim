package dev.mee42.db;

public class Transit extends Location {
    public final DatabaseObject<Location> goingTo;
    public final DatabaseObject<Location> goingFrom;
    public final long started;
    public final long ends;

    Transit(int id,DatabaseObject<Location> goingTo, DatabaseObject<Location> goingFrom, long started, long ends) {
        super(id);
        this.goingTo = goingTo;
        this.goingFrom = goingFrom;
        this.started = started;
        this.ends = ends;
    }
    public static DatabaseObject<Transit> getTransit(int id) {
        return new DatabaseObject<>(id, () -> Database.connection.withHandle((handle) ->
                handle.createQuery("SELECT * FROM transit WHERE id = :id")
                        .bind("id", id)
                        .map((rs, ctx) ->
                                new Transit(rs.getInt("id"),
                                        Location.get(rs.getInt("goingTo")),
                                        Location.get(rs.getInt("goingFrom")),
                                        rs.getLong("started"),
                                        rs.getLong("ends"))
                        )
                        .list()
        ).stream().findFirst().orElse(null));
    }

    @Override
    public String toString() {
        return "Transit{" +
                "id=" + id +
                ", goingTo=" + goingTo +
                ", goingFrom=" + goingFrom +
                ", started=" + started +
                ", ends=" + ends +
                '}';
    }
}
