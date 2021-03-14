package dev.mee42.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.mee42.discord.Discord;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import org.bson.Document;

import java.time.Instant;
import java.util.Objects;

public class Database {
    public static final Database inst = new Database();
    private final MongoClient client = MongoClients.create("mongodb://root:rootpassword@127.0.0.1");
    public final MongoDatabase db = client.getDatabase("ps");
    private Database() {
        System.out.println("Connected to Database");

    }

   
    public void reset() {
       var members = Discord.gateway.getGuildById(Snowflake.of(697514864897163284L)).flatMapMany(Guild::getMembers)
                .map(m -> new Document("_id", m.getId().asLong())
                        .append("name", m.getUsername()).append("mines", 0).append("last_mine", Instant.EPOCH.toEpochMilli()))
                .collectList().block();

        db.getCollection("users").drop();
        db.getCollection("users").insertMany(Objects.requireNonNull(members));
    }
}

/*

 schema:

 collection users:
   _id: their discord id
   name: their display name
   mines: an int, how many times have they mined
   last_mine: a unix timestamp, the instant they last mined



*/





