package com.gmail.hc.gwnoii.movie.model;

import java.util.ArrayList;

public class MovieDetailList extends BasicListClass {

    private ArrayList<MovieDetail> result = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public float getCode() {
        return code;
    }

    public String getResultType() {
        return resultType;
    }

    public ArrayList<MovieDetail> getResult() {
        return result;
    }
}



