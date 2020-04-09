package dev.mee42.db;

import dev.mee42.Getter;

public class DatabaseObject<T> {
    private final Getter<T> getter;
    private int id;
    private IDer<T> ider;
    private T cache = null;

    public int getId() {
        if(id == -1) {
            if(ider == null) throw new RuntimeException("ohno");
            id = ider.id(get());
        }
        return id;
    }
    public T get() {
        if(cache == null) cache = getter.get();
        return cache;
    }

    public DatabaseObject(int id, Getter<T> getter) {
        this.getter = getter;
        this.id = id;
        this.ider = null;
    }
    interface IDer<T> {
        int id(T t);
    }
    public DatabaseObject(IDer<T> ider, Getter<T> getter) {
        this.getter = getter;
        this.id = -1;
        this.ider = ider;
    }


    @Override
    public String toString() {
        return "DatabaseObject(" + id + ")";
    }
}
