package com.gmail.hc.gwnoii.movie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetail implements Parcelable {

    private String title;
    private float id;
    private String date;
    private float user_rating;
    private float audience_rating;
    private float reviewer_rating;
    private float reservation_rate;
    private float reservation_grade;
    private float grade;
    private String thumb;
    private String image;
    private String photos;
    private String videos;
    private String outlinks;
    private String genre;
    private float duration;
    private float audience;
    private String synopsis;
    private String director;
    private String actor;
    private float like;
    private float dislike;

    public MovieDetail() {

    }

    public MovieDetail(String title, float id, String date, float user_rating, float audience_rating
            , float reviewer_rating, float reservation_rate, float reservation_grade
            , float grade, String thumb, String image, String photos, String videos
            , String outlinks, String genre, float duration, float audience, String synopsis
            , String director, String actor, float like, float dislike) {

        this.title = title;
        this.id = id;
        this.date = date;
        this.user_rating = user_rating;
        this.audience_rating = audience_rating;
        this.reviewer_rating = reviewer_rating;
        this.reservation_rate = reservation_rate;
        this.reservation_grade = reservation_grade;
        this.grade = grade;
        this.thumb = thumb;
        this.image = image;
        this.photos = photos;
        this.videos = videos;
        this.outlinks = outlinks;
        this.genre = genre;
        this.duration = duration;
        this.audience = audience;
        this.synopsis = synopsis;
        this.director = director;
        this.actor = actor;
        this.like = like;
        this.dislike = dislike;
    }

    private MovieDetail(Parcel data) {
        title = data.readString();
        id = data.readFloat();
        date = data.readString();
        user_rating = data.readFloat();
        audience_rating = data.readFloat();
        reviewer_rating = data.readFloat();
        reservation_rate = data.readFloat();
        reservation_grade = data.readFloat();
        grade = data.readFloat();
        thumb = data.readString();
        image = data.readString();
        photos = data.readString();
        videos = data.readString();
        outlinks = data.readString();
        genre = data.readString();
        duration = data.readFloat();
        audience = data.readFloat();
        synopsis = data.readString();
        director = data.readString();
        actor = data.readString();
        like = data.readFloat();
        dislike = data.readFloat();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public MovieDetail createFromParcel (Parcel data) {
            return new MovieDetail(data);           // Parcel로 부터 데이터를 받고 새로운 SimpleData 객체를 만든다.
        }

        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];            // SimpleData를 기본적으로 array의 형태로 만들어 들어오는 개수에 맞게 array에 저장시킨다.
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeFloat(id);
        dest.writeString(date);
        dest.writeFloat(user_rating);
        dest.writeFloat(audience_rating);
        dest.writeFloat(reviewer_rating);
        dest.writeFloat(reservation_rate);
        dest.writeFloat(reservation_grade);
        dest.writeFloat(grade);
        dest.writeString(thumb);
        dest.writeString(image);
        dest.writeString(photos);
        dest.writeString(videos);
        dest.writeString(outlinks);
        dest.writeString(genre);
        dest.writeFloat(duration);
        dest.writeFloat(audience);
        dest.writeString(synopsis);
        dest.writeString(director);
        dest.writeString(actor);
        dest.writeFloat(like);
        dest.writeFloat(dislike);
    }

    public String getTitle() {
        return title;
    }

    public float getId() {
        return id;
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

    public String getPhotos() {
        return photos;
    }

    public String getVideos() {
        return videos;
    }

    public String getOutlinks() {
        return outlinks;
    }

    public String getGenre() {
        return genre;
    }

    public float getDuration() {
        return duration;
    }

    public float getAudience() {
        return audience;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }

    public float getLike() {
        return like;
    }

    public float getDislike() {
        return dislike;
    }
}
