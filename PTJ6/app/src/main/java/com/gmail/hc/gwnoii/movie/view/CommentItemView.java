package com.gmail.hc.gwnoii.movie.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gmail.hc.gwnoii.movie.R;


public class CommentItemView extends LinearLayout {

    TextView name;
    TextView writeTime;
    TextView comment;
    TextView commentLikeNum;
    RatingBar ratingValue;

    public CommentItemView(Context context) {
        super(context);

        init(context);
    }

    public CommentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.coment_item, this, true);

        name = findViewById(R.id.tv_userId);
        writeTime = findViewById(R.id.tv_writeTime);
        comment = findViewById(R.id.tv_comment);
        commentLikeNum = findViewById(R.id.tv_commentLikeNum);
        ratingValue = findViewById(R.id.rbDoEvaluate);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setWriteTime(String writeTime) {
        this.writeTime.setText(writeTime);
    }

    public void setComment(String comment) {
        this.comment.setText(comment);
    }

    public void setCommentLikeNum(String commentLikeNum) {
        this.commentLikeNum.setText(commentLikeNum);
    }
}
