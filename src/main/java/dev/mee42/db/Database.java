package dev.mee42.db;

import dev.mee42.discord.Discord;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;

public class Database {
    public static final Database inst = new Database();
    public final Jdbi jdbi = Jdbi.create("jdbc:sqlite:data.db");

    private Database() {
        jdbi.registerArgument(new SnowflakeArgumentFactory());
        jdbi.registerRowMapper(new PlayerMapper());
        System.out.println("Connected to Database");
    }

   
    public void reset() {
        

       var members = Discord.gateway.getGuildById(Snowflake.of(697514864897163284L)).flatMapMany(Guild::getMembers)
               .map(it -> new Player(it.getId(), it.getDisplayName(), 0, Instant.EPOCH))
                .collectList().block();

       jdbi.useHandle(h -> {
           h.execute("DROP TABLE IF EXISTS users");

           h.execute("""
CREATE TABLE users(
    id INT PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    mines INT NOT NULL,
    lastMine INT NOT NULL)
""");

           members.forEach(player -> {
               h.createUpdate("INSERT INTO users (id, name, mines, lastMine) VALUES (:id, :name, :mines, :lastMine)")
                .bindFields(player)
                .execute();
           });
       });

    }

    static class SnowflakeArgumentFactory extends AbstractArgumentFactory<Snowflake> {
        SnowflakeArgumentFactory() {
            super(Types.BIGINT);
        }

        @Override
        protected Argument build(Snowflake value, ConfigRegistry config) {
            return ((position, statement, ctx) -> statement.setLong(position, value.asLong()));
        }
    }
    static class PlayerMapper implements RowMapper<Player> {

        @Override
        public Player map(ResultSet rs, StatementContext ctx) throws SQLException {
            return new Player(
                    Snowflake.of(rs.getLong("id")),
                    rs.getString("name"),
                    rs.getInt("mines"),
                    Instant.ofEpochMilli(rs.getLong("lastMine"))
            );
        }
    }
}


/*

 schema:

users:
    id: their discord id
    name: their display name
    last_mine: a unix timestamp, the instant they last mined
    location: a location id, can be a planet, moon, or transit

planets:
    id: randomly generated
    name: their display name, can be changed by the owner of the planet
    star: the star that they orbit

moon:
    id: randomly generated
    name: display name, can be changed
    planet: the planet they orbit

stars:
    id: randomly generated
    name: the name of the star
    xPosition: int
    yPosition: int

transit:
    id: randomly generated
    user: id of the user traveling
    startTime: epoch millis
    endTime: epoch millis, the time it ends
    destination: a location id, either a planet or moon
    origin: a location id, either a planet or moon, of where the user started

*/





