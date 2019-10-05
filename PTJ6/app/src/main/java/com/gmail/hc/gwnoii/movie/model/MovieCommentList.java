package com.gmail.hc.gwnoii.movie.model;

import java.util.ArrayList;

public class MovieCommentList extends BasicListClass {

    private float totalCount;
    private ArrayList<MovieComment> result = new ArrayList<>();


    public String getMessage() {
        return message;
    }

    public float getCode() {
        return code;
    }

    public String getResultType() {
        return resultType;
    }

    public float getTotalCount() {
        return totalCount;
    }

    public ArrayList<MovieComment> getResult() {
        return result;
    }
}
