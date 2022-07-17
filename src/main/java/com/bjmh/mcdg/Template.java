package com.bjmh.mcdg;

public class Template {
    public String contents;
    public String task;
    public String name;
    public String type;

    @Override
    public String toString() {
        return "{name=" + name + ", task=" + task + ", type=" + type + "}";
    }
}
