package com.gmail.hc.gwnoii.movie.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gmail.hc.gwnoii.movie.model.CommentItem;
import com.gmail.hc.gwnoii.movie.view.CommentItemView;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private Context context;

    public CommentAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<CommentItem> commentItems = new ArrayList<>();

    public void addItem(CommentItem commentItem) {
        commentItems.add(commentItem);
    }

    @Override
    public int getCount() {
        return commentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return commentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentItemView commentItemView = new CommentItemView(context);

        CommentItem item = commentItems.get(position);
        commentItemView.setName(item.getName());
        commentItemView.setWriteTime(item.getWriteTime());
        commentItemView.setComment(item.getComment());
        commentItemView.setCommentLikeNum(item.getCommentLikeNum());

        return commentItemView;
    }

    public ArrayList<CommentItem> getCommentItems() {
        return commentItems;
    }
}
