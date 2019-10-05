package com.gmail.hc.gwnoii.movie.model;

public class MovieComment {

    private float id;
    private String writer;
    private float movieId;
    private String writer_image;
    private String time;
    private float timestamp;
    private float rating;
    private String contents;
    private float recommend;

    public float getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }

    public float getMovieId() {
        return movieId;
    }

    public String getWriter_image() {
        return writer_image;
    }

    public String getTime() {
        return time;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public float getRating() {
        return rating;
    }

    public String getContents() {
        return contents;
    }

    public float getRecommend() {
        return recommend;
    }
}
