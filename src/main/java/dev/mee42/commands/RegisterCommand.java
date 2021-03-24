package dev.mee42.commands;

import dev.mee42.discord.Command;
import dev.mee42.discord.Context;

public class RegisterCommand extends Command  {
    public RegisterCommand() {
        super(
                false,
                true,
                false,
                "register",
                "Register for the game",
                "use this command to become a player of planet sim");
    }

    @Override
    public void run(Context context) {
        context.createMessage("Not done with this yet!");
//        Player existing = context.player;
//        if(existing != null) {
//            throw new UserScrewedUpException("You are already registered");
//        }
//        int id = Util.genNewID();
//        Player.getAll().add(new Player(
//                context.authorID,
//                id,
//                context.author.getUsername(),
//                Planet.getEarth()
//        ));
//        context.message.getAuthorAsMember().flatMap(author -> author.edit(spec -> spec.setNickname(author.getDisplayName() + " (" + id + ")"))).block();
//        context.createMessage("Registered!").block();
    }
}
