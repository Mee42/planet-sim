package dev.mee42;

import dev.mee42.db.*;

public class Main {
  public static void main(String[] args) {
    System.err.println("Planet Sim!\n");
    System.err.flush();
//    System.out.println(Player.get(0).get());
//    System.out.println(Location.get(11).get());
//    System.out.println(SolarSystem.get(10).get());

    // lets say
    Player p = Player.get(0).get();
    System.out.println("player: " + p);

    Location location = p.location.get();
    System.out.println(location);
    if(location instanceof Planet) {
      Planet p1 = (Planet)location;
      System.out.println("player is on planet " + p1.name + " in solar system " + p1.solarSystem.get().name);
    } else if(location instanceof Moon){
      Moon m = (Moon)location;
      System.out.println("player is on moon " + m.name + " which is around planet " + m.planet.get().name +
              " in solar system " + m.planet.get().solarSystem.get().name);
    } else if(location instanceof Transit) {
      Transit t = (Transit)location;
      System.out.println("player is in transit from " + t.goingTo + " from " + t.goingFrom + ""); // without get
    }
  }
}
