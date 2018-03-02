package com.douglasharvey.popularmovies.data;

@SuppressWarnings("unused")
public class Video {
    private final String id;
    private final String key;
    private final String name;
    private final String type;

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    public Video(String id, String key, String name, String type) {

        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }
}
