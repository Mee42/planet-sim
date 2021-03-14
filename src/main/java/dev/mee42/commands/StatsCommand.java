package dev.mee42.commands;

import com.mongodb.client.model.Filters;
import dev.mee42.Util;
import dev.mee42.db.*;
import dev.mee42.discord.Command;
import dev.mee42.discord.Context;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StatsCommand extends Command {

    public StatsCommand() {
        super(false, false, false, "stats", "show helpful information", "TODO not done");
    }


    private final Instant startedAt = Instant.now();

    private final String developer = Util.getPasswords().get(2);



    @Override
    public void run(Context context) {
        int blocksMined = Database.inst.db.getCollection("users").find(Filters.eq("_id", context.authorID.asLong())).first().getInteger("mines");
        context.createMessage("you have mined " + blocksMined + " times!").block();
    }

}
