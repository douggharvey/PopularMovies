package com.douglasharvey.popularmovies.data;

public class Review {

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

    private String id;
    private String author;
    private String content;
    private String url;
}
