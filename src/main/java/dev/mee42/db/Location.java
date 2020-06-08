package dev.mee42.db;

import java.util.ArrayList;
import java.util.List;

public abstract class Location {
    public final int id;
    protected Location(int id) {
        this.id = id;
    }

    public abstract String toNiceString();

    public static final List<Location> allLocations = new ArrayList<>();
    public static Location byId(int id) {
        return allLocations.stream().filter(it -> it.id == id).findFirst().orElse(null);
    }
}
