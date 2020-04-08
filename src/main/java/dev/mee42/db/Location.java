package dev.mee42.db;

public abstract class Location {
    final int id;
    protected Location(int id) {
        this.id = id;
    }
    public static DatabaseObject<Location> get(int id) {
        // try all of the 3 databases lol
//        Planet.getPlanet(id).get()
        return new DatabaseObject<Location>(id, () -> {
            Planet planet = Planet.getPlanet(id).get();
            if(planet != null) return planet;
            Moon moon = Moon.getMoon(id).get();
            if(moon != null) return moon;
            return Transit.getTransit(id).get(); // if this isn't found, it'll return null
        });
    }
}
