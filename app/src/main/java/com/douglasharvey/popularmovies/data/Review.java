package com.douglasharvey.popularmovies.data;

public class Review {
    private final String id;
    private final String author;
    private final String content;
    private final String url;

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    @SuppressWarnings("unused")
    public String getUrl() {
        return url;
    }

    public Review(String id, String author, String content, String url) {

        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }


}
