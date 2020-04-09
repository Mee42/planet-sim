package dev.mee42.discord;

public abstract class Command {
    protected Command(boolean stateful, boolean adminCommand, String name, String shortHelp, String longHelp) {
        this.stateful = stateful;
        this.adminCommand = adminCommand;
        this.shortHelp = shortHelp;
        this.longHelp = longHelp;
        this.name = name;
    }

    public abstract void run(Context context);
    public final boolean stateful;
    public final boolean adminCommand;
    public final String shortHelp;
    public final String longHelp;
    public final String name;

}
