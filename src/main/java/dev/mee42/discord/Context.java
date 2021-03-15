package dev.mee42.discord;


import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;


// This class is immutable
public class Context {
    public final Message message;
    public final String argument;
    public final String messageContent;
    public final Snowflake authorID;
    public final User author;


    public Context(Message message, String argument) {
        this.message = message;
        this.argument = argument;
        this.messageContent = message.getContent();
        this.author =  message.getAuthor().orElseThrow(() -> new RuntimeException("oof"));
        this.authorID = author.getId();
    }
    public Mono<Message> createMessageAsync(String content) {
        return message.getChannel().flatMap(c -> c.createMessage(content));
    }
    public Message createMessage(String content) {
        return createMessageAsync(content).block();
    }
    
    public Mono<Message> createEmbedAsync(Consumer<EmbedCreateSpec> spec) {
        return message.getChannel().flatMap(c -> c.createEmbed(spec));
    }
    public Message createEmbed(Consumer<EmbedCreateSpec> spec) {
        return createEmbedAsync(spec).block();
    }
}


