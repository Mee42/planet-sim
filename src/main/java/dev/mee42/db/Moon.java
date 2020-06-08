package dev.mee42.db;

import reactor.util.annotation.Nullable;

import java.util.stream.Stream;

public class Moon extends Location {
    public final String name;
    public final Planet planet;
    public @Nullable Player ownedBy;

    public Moon(int id, String name, Planet planet, @Nullable Player ownedBy) {
        super(id);
        this.name = name;
        this.planet = planet;
        this.ownedBy = ownedBy;
    }

    @Override
    public String toString() {
        return "Moon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", planet=" + planet +
                '}';
    }


    public static Stream<Moon> getAll(){
        return Location.allLocations.stream().filter(it -> it instanceof Moon).map(it -> (Moon) it);
    }
    @Override
    public String toNiceString() {
        return "moon \"" + name + "\"(" + id + ") orbiting " + planet.toNiceString();
    }
}
