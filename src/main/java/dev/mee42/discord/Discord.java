package dev.mee42.discord;

import dev.mee42.Util;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.gateway.intent.Intent;
import discord4j.gateway.intent.IntentSet;

import java.util.ArrayList;
import java.util.List;

public class Discord {
    public static final DiscordClient client = DiscordClientBuilder.create(Util.getSecrets().get(1).trim()).build();
    public static final GatewayDiscordClient gateway = client
            .gateway()
            .setEnabledIntents(IntentSet.nonPrivileged().or(IntentSet.of(Intent.GUILD_MEMBERS)))
            .login()
            .blockOptional().get();
    public static final List<Command> commands = new ArrayList<>();
}
