package dev.mee42.discord;

import dev.mee42.Util;
import discord4j.core.DiscordClient;

import java.util.ArrayList;
import java.util.List;

public class Discord {
    public static final DiscordClient client = DiscordClient.create(Util.getPasswords().get(1).trim());
    public static final List<Command> commands = new ArrayList<>();
}
