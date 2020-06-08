package dev.mee42.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import discord4j.core.object.util.Snowflake;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class Saver {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    private static final File backupDir = new File("saves/backups/");

    private static final File saveDir = new File("saves/mainSave/");
    private static final File playerDir = new File(saveDir, "players");
    private static final File ssDir = new File(saveDir, "ss");
    private static final File planetDir = new File(saveDir, "planets");
    private static final File moonDir = new File(saveDir, "moons");


    public static void save() throws IOException, InterruptedException {
        if(saveDir.exists()) {
            // move it to a new dir, so we don't actually delete the file
            backupDir.mkdirs();
            new ProcessBuilder("mv", saveDir.getAbsolutePath(), new File(backupDir, UUID.randomUUID().toString()).getAbsolutePath())
                    .inheritIO()
                    .start()
                    .waitFor();
        }
        saveDir.mkdirs();


        for(Player p : Player.getAll()) {
            PlayerBean bean = new PlayerBean(p.id, p.discordID.asLong(), p.name, p.location.id);
            writeTo(new File(playerDir, p.id + ".json"), bean);
        }

        for(SolarSystem ss : SolarSystem.getAll()) {
            SSBean bean = new SSBean(ss.id, ss.name, ss.xPos, ss.yPos);
            writeTo(new File(ssDir, ss.id + ".json"), bean);
        }

        for(Planet p: Planet.getAll().collect(toList())) {
            PlanetBean bean = new PlanetBean(p.id, p.name, p.solarSystem.id, p.ownedBy == null ? -1 : p.ownedBy.id);
            writeTo(new File(planetDir, p.id + ".json"), bean);
        }

        for(Moon p: Moon.getAll().collect(toList())) {
            MoonBean bean = new MoonBean(p.id, p.name, p.planet.id, p.ownedBy==null ? -1 : p.ownedBy.id);
            writeTo(new File(moonDir, p.id + ".json"), bean);
        }

    }
    private static <T> void writeTo(File file, T obj) throws IOException {
        file.getParentFile().mkdirs();
        Files.writeString(file.toPath(), gson.toJson(obj));
    }
    private static <T> T readFrom(File file, Class<T> clazz) throws IOException {
        return gson.fromJson(Files.readString(file.toPath()), clazz);
    }

    public static void load() throws IOException {
        if(!saveDir.exists()) throw new RuntimeException("game save dir does not exist, bruh what");

        List<SolarSystem> loadedSSs = SolarSystem.getAll();
        loadedSSs.clear();
        for(File f : Objects.requireNonNull(ssDir.listFiles())) {// ssDir.listFiles() will be null if ssDir is a file
            SSBean bean = readFrom(f, SSBean.class);
            loadedSSs.add(new SolarSystem(bean.id, bean.name, bean.xPos, bean.yPos));
        }

        List<Planet>     planets     = new ArrayList<>();
        List<PlanetBean> planetBeans = new ArrayList<>();

        for(File f: Objects.requireNonNull(planetDir.listFiles())) {
            PlanetBean bean = readFrom(f, PlanetBean.class);
            planets.add(new Planet(bean.name, SolarSystem.getById(bean.solarSystem), null, bean.id));
            planetBeans.add(bean);
        }

        List<Moon> moons = new ArrayList<>();
        List<MoonBean> moonBeans = new ArrayList<>();

        for(File f: Objects.requireNonNull(moonDir.listFiles())) {
            MoonBean bean = readFrom(f, MoonBean.class);
            moons.add(new Moon(
                    bean.id,
                    bean.name,
                    planets.stream().filter(it -> it.id == bean.planetID).findFirst().get(),
                    null));
            moonBeans.add(bean);
        }

        // no transits yet lol, so we won't save those
        Location.allLocations.clear();
        Location.allLocations.addAll(planets);
        Location.allLocations.addAll(moons);
        // okay, now we have all the locations
        // we can add all the players with a simple lookup
        Player.getAll().clear();

        for(File f: Objects.requireNonNull(playerDir.listFiles())) {
            PlayerBean bean = readFrom(f, PlayerBean.class);
            Player.getAll().add(new Player(
                    Snowflake.of(bean.discordID),
                    bean.id,
                    bean.name,
                    Location.byId(bean.locationID)
            ));
        }
        // okay now we go through and add ownership data for planets and moons

        for(int i = 0; i < planets.size(); i++) {
            PlanetBean bean = planetBeans.get(i);
            if(bean.ownedBy != -1) {
                planets.get(i).ownedBy = Player.getById(bean.ownedBy);
            }
        }
        for(int i = 0; i < moons.size(); i++) {
            MoonBean bean = moonBeans.get(i);
            if(bean.ownedBy != -1) {
                moons.get(i).ownedBy = Player.getById(bean.ownedBy);
            }
        }


    }



    static class PlayerBean {
        final int id;
        final long discordID;
        final String name;
        final int locationID;
        PlayerBean(int id, long discordID, String name, int locationID) {
            this.id = id;
            this.discordID = discordID;
            this.name = name;
            this.locationID = locationID;
        }
    }
    static class PlanetBean {
        final int id;
        final String name;
        final int solarSystem;
        final int ownedBy;

        PlanetBean(int id, String name, int solarSystem, int ownedBy) {
            this.id = id;
            this.name = name;
            this.solarSystem = solarSystem;
            this.ownedBy = ownedBy;
        }
    }
    static class SSBean {
        final int id;
        final String name;
        final int xPos;
        final int yPos;

        SSBean(int id, String name, int xPos, int yPos) {
            this.id = id;
            this.name = name;
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }
    static class MoonBean {
        final int id;
        final String name;
        final int planetID;
        final int ownedBy;

        MoonBean(int id, String name, int planetID, int ownedBy) {
            this.id = id;
            this.name = name;
            this.planetID = planetID;
            this.ownedBy = ownedBy;
        }
    }
}
