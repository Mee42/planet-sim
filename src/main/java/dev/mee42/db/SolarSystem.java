package dev.mee42.db;

import java.util.ArrayList;
import java.util.List;

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

    public SolarSystem(int id, String name, int xPos, int yPos) {
        this.id = id;
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
    }


    private static final List<SolarSystem> all = new ArrayList<>();
    public static List<SolarSystem> getAll() { return all; }
    public static SolarSystem getById(int id) { return all.stream().filter(it -> it.id == id).findFirst().orElse(null); }
}
