package dev.mee42.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

public class Transit extends Location {
    public final Location goingTo;
    public final Location goingFrom;
    public final long started;
    public final long ends;

    Transit(int id,Location goingTo, Location goingFrom, long started, long ends) {
        super(id);
        this.goingTo = goingTo;
        this.goingFrom = goingFrom;
        this.started = started;
        this.ends = ends;
    }
    @Override
    public String toString() {
        return "Transit{" +
                "id=" + id +
                ", goingTo=" + goingTo +
                ", goingFrom=" + goingFrom +
                ", started=" + started +
                ", ends=" + ends +
                '}';
    }

    private String parseTimestamp(Instant time) {
        TimeZone tz = TimeZone.getTimeZone("UST");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        return df.format(time);
    }

    @Override
    public String toNiceString() {
        return "transit, going from " + goingFrom.toNiceString() + " to " + goingTo.toNiceString() +
                ", getting there " + parseTimestamp(Instant.ofEpochMilli(ends)) + " (" + id + ")";
    }
}