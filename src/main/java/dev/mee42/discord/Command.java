package dev.mee42.discord;

public abstract class Command {
    protected Command(boolean needsAccount,
                      boolean stateful,
                      boolean adminCommand,
                      String name,
                      String shortHelp,
                      String longHelp) {
        this.needsAccount = needsAccount;
        this.stateful = stateful;
        this.adminCommand = adminCommand;
        this.shortHelp = shortHelp;
        this.longHelp = longHelp;
        this.name = name;
    }

    public abstract void run(Context context);

    public final boolean needsAccount; // if the user needs to registered in ps
    public final boolean stateful; /// if the command is stateful (why do we need this?)
    public final boolean adminCommand; // if only @Admins should be able to runt his command
    public final String shortHelp;
    public final String longHelp;
    public final String name; // the keyword used after 'ps', for example the 'mine' in 'ps mine'

}
