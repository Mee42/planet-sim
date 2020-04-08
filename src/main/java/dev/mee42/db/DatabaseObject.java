package dev.mee42.db;

import dev.mee42.Getter;

public class DatabaseObject<T> {
    private final Getter<T> getter;
    final int id;

    public DatabaseObject(int id, Getter<T> getter) {
        this.getter = getter;
        this.id = id;
    }

    public T get() {
        return getter.get();
    }

    @Override
    public String toString() {
        return "DatabaseObject(" + id + ")";
    }
}
