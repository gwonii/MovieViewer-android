package com.gmail.hc.gwnoii.movie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private float id;
    private String title;
    private String title_eng;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float reviewer_rating;
    private float reservation_rate;
    private float reservation_grade;
    private float grade;
    private String thumb;
    private String image;

    public Movie() {

    }

    public Movie(float id, String title, String title_eng, String date, float user_rating
            , float audience_rating, float reviewer_rating, float reservation_rate
            , float reservation_grade, float grade, String thumb, String image) {

        this.id = id;
        this.title = title;
        this.title_eng = title_eng;
        this.date = date;
        this.user_rating = user_rating;
        this.audience_rating = audience_rating;
        this.reviewer_rating = reviewer_rating;
        this.reservation_rate = reservation_rate;
        this.reservation_grade = reservation_grade;
        this.grade = grade;
        this.thumb = thumb;
        this.image = image;
    }

    private Movie (Parcel data) {
        id = data.readFloat();
        title = data.readString();
        title_eng = data.readString();
        date = data.readString();
        user_rating = data.readFloat();
        audience_rating = data.readFloat();
        reviewer_rating = data.readFloat();
        reservation_rate = data.readFloat();
        reservation_grade = data.readFloat();
        grade = data.readFloat();
        thumb = data.readString();
        image = data.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Movie createFromParcel (Parcel data) {
            return new Movie(data);           // Parcel로 부터 데이터를 받고 새로운 SimpleData 객체를 만든다.
        }

        public Movie[] newArray(int size) {
            return new Movie[size];            // SimpleData를 기본적으로 array의 형태로 만들어 들어오는 개수에 맞게 array에 저장시킨다.
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeFloat(id);
        dest.writeString(title);
        dest.writeString(title_eng);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(reviewer_rating);
        dest.writeFloat(reservation_rate);
        dest.writeFloat(reservation_grade);
        dest.writeFloat(grade);
        dest.writeString(thumb);
        dest.writeString(image);
    }

    public float getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTitle_eng() {
        return title_eng;
    }

    public String getDate() {
        return date;
    }

    public float getUser_rating() {
        return user_rating;
    }

    public float getAudience_rating() {
        return audience_rating;
    }

    public float getReviewer_rating() {
        return reviewer_rating;
    }

    public float getReservation_rate() {
        return reservation_rate;
    }

    public float getReservation_grade() {
        return reservation_grade;
    }

    public float getGrade() {
        return grade;
    }

    public String getThumb() {
        return thumb;
    }

    public String getImage() {
        return image;
    }

    public void setId(float id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle_eng(String title_eng) {
        this.title_eng = title_eng;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUser_rating(float user_rating) {
        this.user_rating = user_rating;
    }

    public void setAudience_rating(float audience_rating) {
        this.audience_rating = audience_rating;
    }

    public void setReviewer_rating(float reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public void setReservation_rate(float reservation_rate) {
        this.reservation_rate = reservation_rate;
    }

    public void setReservation_grade(float reservation_grade) {
        this.reservation_grade = reservation_grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
