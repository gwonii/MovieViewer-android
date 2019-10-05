package com.gmail.hc.gwnoii.movie.model;

import java.util.ArrayList;

public class MovieList extends BasicListClass {

    private ArrayList<Movie> result = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public float getCode() {
        return code;
    }

    public String getResultType() {
        return resultType;
    }

    public ArrayList<Movie> getResult() {
        return result;
    }
}


