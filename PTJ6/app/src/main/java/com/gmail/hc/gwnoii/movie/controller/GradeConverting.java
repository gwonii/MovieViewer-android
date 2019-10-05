package com.gmail.hc.gwnoii.movie.controller;

import com.gmail.hc.gwnoii.movie.R;

public class GradeConverting {

    public static int setGrade(String grade) {

        if (grade.equals("12")) {
            return R.drawable.ic_12;
        } else if (grade.equals("15")) {
            return R.drawable.ic_15;
        } else if (grade.equals("19")) {
            return R.drawable.ic_19;
        } else {
            return R.drawable.ic_all;
        }
    }
}
