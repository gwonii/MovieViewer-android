package com.gmail.hc.gwnoii.movie.controller;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.hc.gwnoii.movie.R;
import com.gmail.hc.gwnoii.movie.model.CommentItem;
import com.gmail.hc.gwnoii.movie.model.Movie;
import com.gmail.hc.gwnoii.movie.model.MovieComment;
import com.gmail.hc.gwnoii.movie.model.MovieCommentList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentListActivity extends AppCompatActivity {

    private static final int COMMENT_VIEW = 10;

    private CommentAdapter adapter = new CommentAdapter(this);
    private MovieCommentList movieCommentList;
    ArrayList<MovieComment> sendedData;

    private ListView allListView;
    private Movie movieDetail;

    TextView writeButton;
    TextView title;
    ImageView grade;
    RatingBar ratingBar;
    TextView ratingValue;
    TextView totalCount;
    int movieId;

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        writeButton = findViewById(R.id.tvCommentListWrite);
        title = findViewById(R.id.tvCommentListMovieTitle);
        grade = findViewById(R.id.ivCommentListAge);
        ratingBar = findViewById(R.id.rbDoEvaluate);
        ratingValue = findViewById(R.id.tvRatingValue);
        totalCount = findViewById(R.id.tvParticipants);

        allListView = findViewById(R.id.lvCommentListReview);

        Intent intent = getIntent();
        processIntent(intent);

        String sendedTitle = movieDetail.getTitle();
        title.setText(sendedTitle);

        String sendedGrade = String.valueOf((int) movieDetail.getGrade());
        int drawableId = GradeConverting.setGrade(sendedGrade);
        grade.setImageResource(drawableId);

        float passedRatingBar = movieDetail.getUser_rating();
        ratingBar.setRating(passedRatingBar);

        String passedRatingValue = String.valueOf(movieDetail.getAudience_rating());
        ratingValue.setText(passedRatingValue);

//
        if (NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
            processDatabaseComment();
        } else {                                // DetailView에서도 인터넷이 연결되어 있으면, API 정보를 받고, 그렇지 않으면, 데이터베이스의 정보를 받는다.
            sendRequestComment(movieId);
        }

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (NetworkStatus.getConnectivityStatus(getApplicationContext()) == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(getApplicationContext(), "인터넷을 연결해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    // 여기 작성하기 전에 CommentList에서 직접 데이터 받아오기!!!
                    //
                    Intent intent = new Intent(getApplicationContext(), DoCommentActivity.class);
                    intent.putExtra("movieData", movieDetail);
                    startActivityForResult(intent, 1007);
                }
            }
        });
    }

    private void processIntent(Intent passedIntent) {
        movieDetail = passedIntent.getParcelableExtra("movieData");
        movieId = (int) movieDetail.getId();
    }

    private void sendRequestComment(int movieId) {

        String tempNum = Integer.toString(movieId);

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readCommentList";
        url += "?" + "id=" + tempNum;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponseComment(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    private void processResponseComment(String response) {
        Gson gson = new Gson();
        movieCommentList = gson.fromJson(response, MovieCommentList.class);

        sendedData = movieCommentList.getResult();
        String CommentTableName;

        if (AppHelper.getDatabase() != null) {
            CommentTableName = "Comment" + movieId;

            AppHelper.createCommentTable("Comment", getApplicationContext(), movieId);

            String sql = "insert or replace into " + CommentTableName + "(id, writer, movieId, writer_image, time, " +
                    "timestamp, rating, contents, recommend, total_count)" + " values(?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < sendedData.size(); i++) {
                Object[] params = {sendedData.get(i).getId(), sendedData.get(i).getWriter(), sendedData.get(i).getMovieId(), sendedData.get(i).getWriter_image()
                        , sendedData.get(i).getTime(), sendedData.get(i).getTimestamp(), sendedData.get(i).getRating()
                        , sendedData.get(i).getContents(), sendedData.get(i).getRecommend(), movieCommentList.getTotalCount()};

                AppHelper.getDatabase().execSQL(sql, params);
            }
        }

        // TotalCount는 API 요청으로 받은 데이터를 사용해야되기 때문에
        String passedTotalCount = "(" + ((int) movieCommentList.getTotalCount()) + " 명 참여)";
        totalCount.setText(passedTotalCount);

        // 정보 받는건 댓글 관련 데이터이기 때문에 댓글 관련한 어댑터만 여기서 만든다.
        for (int i = 0; i < COMMENT_VIEW; i++) {
            String tempWriter = sendedData.get(i).getWriter();
            String tempTime = sendedData.get(i).getTime();
            String tempContents = sendedData.get(i).getContents();
            String tempRating = String.valueOf(sendedData.get(i).getRating());
            adapter.addItem(new CommentItem(tempWriter, tempTime, tempContents, tempRating));
        }

        allListView.setAdapter(adapter);
    }

    private void processDatabaseComment() {

        String CommentTableName = "Comment" + movieId;

        String sql = "select writer, time, contents, rating, total_count from " + CommentTableName;        // 찾을 내용 수정
        Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);

        for (int i = 0; i < COMMENT_VIEW; i++) {
            cursor.moveToNext();

            String tempWriter = cursor.getString(0);
            String tempTime = cursor.getString(1);
            String tempContents = cursor.getString(2);
            String tempRating = String.valueOf(cursor.getFloat(3));

            adapter.addItem(new CommentItem(tempWriter, tempTime, tempContents, tempRating));
        }

        String passedTotalCount = "(" + ((int) cursor.getFloat(4)) + " 명 참여)";
        totalCount.setText(passedTotalCount);

        allListView.setAdapter(adapter);

        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1007) {
            finish();
            startActivity(getIntent());
        }
    }
}
