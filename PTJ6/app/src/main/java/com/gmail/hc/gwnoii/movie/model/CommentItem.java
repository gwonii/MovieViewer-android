package com.gmail.hc.gwnoii.movie.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem implements Parcelable {

    private String name;
    private String writeTime;
    private String comment;
    private String commentLikeNum;
    private float totalCount;

    public CommentItem(String name, String writeTime, String comment, String commentLikeNum) {
        this.name = name;
        this.writeTime = writeTime;
        this.comment = comment;
        this.commentLikeNum = commentLikeNum;
    }

    private CommentItem (Parcel data) {
        name = data.readString();
        writeTime = data.readString();
        comment = data.readString();
        commentLikeNum = data.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public CommentItem createFromParcel(Parcel source) {
            return new CommentItem(source);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(writeTime);
        dest.writeString(comment);
        dest.writeString(commentLikeNum);
    }

    public String getName() {
        return name;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public String getComment() {
        return comment;
    }

    public String getCommentLikeNum() {
        return commentLikeNum;
    }


    @Override
    public String toString() {
        return "CommentItem{" +
                "name='" + name + '\'' +
                ", writeTime='" + writeTime + '\'' +
                ", comment='" + comment + '\'' +
                ", commentLikeNum='" + commentLikeNum + '\'' +
                '}';
    }
}
