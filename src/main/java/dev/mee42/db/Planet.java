package dev.mee42.db;

import dev.mee42.Util;
import reactor.util.annotation.Nullable;

import java.util.stream.Stream;

import static dev.mee42.Util.*;

public class Planet extends Location {
    public final String name;
    public final SolarSystem solarSystem;
    @Nullable public Player ownedBy;

    public Planet(String name, SolarSystem solarSystem, @Nullable Player ownedBy, int id) {
        super(id);
        requireNonNull(name, solarSystem);
        this.name = name;
        this.solarSystem = solarSystem;
        this.ownedBy = ownedBy;
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

    @Override
    public String toNiceString() {
        return "planet \"" + name + "\"(id: " + id + ")" + " in solar system \"" + solarSystem.name + "\" (id: " + solarSystem.id + ")";
    }

    public static Stream<Planet> getAll(){
        return Location.allLocations.stream().filter(it -> it instanceof Planet).map(it -> (Planet) it);
    }
    public static Planet getEarth(){
        return (Planet)Location.byId(11); // earth is always id 11
    }
}
