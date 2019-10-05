package com.gmail.hc.gwnoii.movie.controller;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gmail.hc.gwnoii.movie.R;
import com.gmail.hc.gwnoii.movie.model.CommentItem;
import com.gmail.hc.gwnoii.movie.model.Movie;
import com.gmail.hc.gwnoii.movie.model.MovieComment;
import com.gmail.hc.gwnoii.movie.model.MovieCommentList;
import com.gmail.hc.gwnoii.movie.model.MovieDetail;
import com.gmail.hc.gwnoii.movie.model.MovieDetailList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailViewFragment extends Fragment {

    MainActivity mainActivity;

    public static final int DO_COMMENT_ACTIVITY_REQUEST_CODE = 1001;
    public static final String TAG = "TagMainActivity";

    private int movieId;

    private ListView listView;

    private TextView tvWrite;
    private TextView tvAllView;
    private ImageView ivAllView;

    private ImageView btLike;
    private ImageView btUnlike;

    // 정보를 받아서 갱신해야 하는 뷰
    private ImageView mainImage;
    private TextView title;
    private ImageView grade;
    private TextView information;
    private TextView reservationRating;
    private TextView audienceRating;
    private TextView audienceNum;
    private TextView story;
    private TextView directorName;
    private TextView castName;
    private TextView tvLikeNum;
    private TextView tvUnlikeNum;

    private CommentAdapter adapter;

    private String CommentTableName;

    private MovieDetailList movieDetailList;
    private MovieDetail deliveredData;
    private MovieCommentList movieCommentList;
    private ArrayList<MovieComment> sendedData = new ArrayList<>();

    private ViewGroup rootView;

    public DetailViewFragment(int movieId) {
        this.movieId = movieId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.detail_view, container, false);

        tvWrite = rootView.findViewById(R.id.tv_write);
        tvAllView = rootView.findViewById(R.id.tv_all_view);
        ivAllView = rootView.findViewById(R.id.iv_all_view_button);

        btLike = rootView.findViewById(R.id.bt_like);
        btUnlike = rootView.findViewById(R.id.bt_unlike);

        mainImage = rootView.findViewById(R.id.iv_movie_poster);
        title = rootView.findViewById(R.id.tv_title);
        grade = rootView.findViewById(R.id.ic_age);
        information = rootView.findViewById(R.id.tv_information);
        reservationRating = rootView.findViewById(R.id.tv_ticket_rating_value);
        audienceRating = rootView.findViewById(R.id.tv_evaluating_value);
        audienceNum = rootView.findViewById(R.id.tv_attendance_num);
        story = rootView.findViewById(R.id.tv_first_story);
        directorName = rootView.findViewById(R.id.tv_director_name);
        castName = rootView.findViewById(R.id.tv_cast_name);
        tvLikeNum = rootView.findViewById(R.id.tv_like_num);
        tvUnlikeNum = rootView.findViewById(R.id.tv_unlike_num);

        if (NetworkStatus.getConnectivityStatus(mainActivity) == NetworkStatus.TYPE_NOT_CONNECTED) {
            processDatabase();
            processDatabaseComment();
        } else {                                // DetailView에서도 인터넷이 연결되어 있으면, API 정보를 받고, 그렇지 않으면, 데이터베이스의 정보를 받는다.
            sendRequest(movieId);
            sendRequestComment(movieId);
        }

        clickEvent();

        return rootView;
    }

    private void clickEvent() {

        tvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), DoCommentActivity.class);

                if (NetworkStatus.getConnectivityStatus(mainActivity) == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(mainActivity, "인터넷을 연결해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    // movie 객체로 보낼 것 !
                    String sql = "select id, title, grade" +
                            " from " + "Movie" + " where id = " + (float) movieId;        // 찾을 내용 수정
                    Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);
                    cursor.moveToFirst();

                    Movie tempMovie = new Movie();

                    tempMovie.setId(cursor.getFloat(0));
                    tempMovie.setTitle(cursor.getString(1));
                    tempMovie.setGrade(cursor.getFloat(2));

                    intent.putExtra("movieData", tempMovie);

                    startActivityForResult(intent, 1005);
                }
            }
        });

        ivAllView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity.getApplicationContext(), CommentListActivity.class);

                String sql = "select id, title, grade, user_rating, audience_rating" +
                        " from " + "Movie" + " where id = " + (float) movieId;        // 찾을 내용 수정
                Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);
                cursor.moveToFirst();

                Movie tempMovie = new Movie();

                tempMovie.setId(cursor.getFloat(0));
                tempMovie.setTitle(cursor.getString(1));
                tempMovie.setGrade(cursor.getFloat(2));
                tempMovie.setUser_rating(cursor.getFloat(3));
                tempMovie.setAudience_rating(cursor.getFloat(4));

                intent.putExtra("movieData", tempMovie);

                startActivityForResult(intent,1006);
            }
        });

        btLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkStatus.getConnectivityStatus(mainActivity) == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(mainActivity, "인터넷을 연결해주세요.", Toast.LENGTH_LONG).show();
                } else {                                // DetailView에서도 인터넷이 연결되어 있으면, API 정보를 받고, 그렇지 않으면, 데이터베이스의 정보를 받는다.
                    if (!btLike.isActivated()) {
                        if (btUnlike.isActivated()) {
                            btUnlike.setActivated(false);
                            int unlikeNum = Integer.parseInt(tvUnlikeNum.getText().toString());
                            tvUnlikeNum.setText(substractAndToString(unlikeNum));

                            btLike.setActivated(true);
                            int likeNum = Integer.parseInt(tvLikeNum.getText().toString());
                            tvLikeNum.setText(addAndToString(likeNum));
                        } else {
                            btLike.setActivated(true);
                            int likeNum = Integer.parseInt(tvLikeNum.getText().toString());
                            tvLikeNum.setText(addAndToString(likeNum));
                        }
                    } else {
                        btLike.setActivated(false);
                        int likeNum = Integer.parseInt(tvLikeNum.getText().toString());
                        tvLikeNum.setText(substractAndToString(likeNum));
                    }
                }
            }
        });

        btUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkStatus.getConnectivityStatus(mainActivity) == NetworkStatus.TYPE_NOT_CONNECTED) {
                    Toast.makeText(mainActivity, "인터넷을 연결해주세요.", Toast.LENGTH_LONG).show();
                } else {                                // DetailView에서도 인터넷이 연결되어 있으면, API 정보를 받고, 그렇지 않으면, 데이터베이스의 정보를 받는다.
                    if (!btUnlike.isActivated()) {
                        if (btLike.isActivated()) {
                            btLike.setActivated(false);
                            int likeNum = Integer.parseInt(tvLikeNum.getText().toString());
                            tvLikeNum.setText(substractAndToString(likeNum));

                            btUnlike.setActivated(true);
                            int unlikeNum = Integer.parseInt(tvUnlikeNum.getText().toString());
                            tvUnlikeNum.setText(addAndToString(unlikeNum));
                        } else {
                            btUnlike.setActivated(true);
                            int unlikeNum = Integer.parseInt(tvUnlikeNum.getText().toString());
                            tvUnlikeNum.setText(addAndToString(unlikeNum));
                        }
                    } else {
                        btUnlike.setActivated(false);
                        int unlikeNum = Integer.parseInt(tvUnlikeNum.getText().toString());
                        tvUnlikeNum.setText(substractAndToString(unlikeNum));
                    }
                }
            }
        });
    }


    private String substractAndToString(int num) {
        String substractedNum;
        substractedNum = Integer.toString(num - 1);
        return substractedNum;
    }

    private String addAndToString(int num) {
        String addedNum;
        addedNum = Integer.toString(num + 1);
        return addedNum;
    }

    private void sendRequest(int movieId) {

        String tempId = Integer.toString(movieId);

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovie";
        url += "?" + "id=" + tempId;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
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

    private void processResponse(String response) {
        Gson gson = new Gson();
        movieDetailList = gson.fromJson(response, MovieDetailList.class);

        deliveredData = movieDetailList.getResult().get(0);

        if (AppHelper.getDatabase() != null) {
            AppHelper.createMovieDetailTable("MovieDetail", mainActivity);                                                                        // Movie table 생성

            String sql = "insert or replace into " + "MovieDetail" + "(id, title, date, user_rating, audience_rating, reviewer_rating, reservation_rate, " +          // table에 데이터 삽입
                    "reservation_grade, grade, thumb, image, photos, videos, outlinks, genre, duration, audience, synopsis, director, " +
                    "actor, like_num, dislike_num)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            Object[] params = {deliveredData.getId(), deliveredData.getTitle(), deliveredData.getDate(),
                    deliveredData.getUser_rating(), deliveredData.getAudience_rating(), deliveredData.getReviewer_rating(),
                    deliveredData.getReservation_rate(), deliveredData.getReservation_grade(), deliveredData.getGrade(),
                    deliveredData.getThumb(), deliveredData.getImage(), deliveredData.getPhotos(), deliveredData.getVideos(),
                    deliveredData.getOutlinks(), deliveredData.getGenre(), deliveredData.getDuration(), deliveredData.getAudience(),
                    deliveredData.getSynopsis(), deliveredData.getDirector(), deliveredData.getActor(), deliveredData.getLike(),
                    deliveredData.getDislike()};

            AppHelper.getDatabase().execSQL(sql, params);
        }

        String tempTitle = deliveredData.getTitle();
        title.setText(tempTitle);

        String tempGrade = String.valueOf((int) deliveredData.getGrade());

        int drawableId = GradeConverting.setGrade(tempGrade);
        grade.setImageResource(drawableId);


        String tempInformation = deliveredData.getDate() + "개봉\n" + deliveredData.getGenre() + " / " + (int) deliveredData.getDuration() + "분";
        information.setText(tempInformation);

        String tempReservationRating = (int) deliveredData.getReservation_grade() + "위 " + deliveredData.getReservation_rate() + "%";
        reservationRating.setText(tempReservationRating);

        String tempAudienceRating = String.valueOf(deliveredData.getAudience_rating());
        audienceRating.setText(tempAudienceRating);

        String tempAudience = String.valueOf((int) deliveredData.getAudience());
        audienceNum.setText(tempAudience);

        String tempStory = deliveredData.getSynopsis();
        story.setText(tempStory);

        String tempDirectorName = deliveredData.getDirector();
        directorName.setText(tempDirectorName);

        String tempCastName = deliveredData.getActor();
        castName.setText(tempCastName);

        String tempLikeNum = Integer.toString((int) deliveredData.getLike());
        tvLikeNum.setText(tempLikeNum);

        String tempUnlikeNum = Integer.toString((int) deliveredData.getDislike());
        tvUnlikeNum.setText(tempUnlikeNum);

        Glide.with(mainActivity)
                .load(deliveredData.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainImage);

    }

    private void processDatabase() {

        String sql = "select title, grade, date, genre, duration, reservation_grade, reservation_rate, " +
                "audience_rating, audience, synopsis, director, actor, like_num, dislike_num, image" +
                " from " + "MovieDetail" + " where id = " + (float) movieId;        // 찾을 내용 수정
        Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();


        try {
            String tempTitle = cursor.getString(0);
            title.setText(tempTitle);

            String tempGrade = String.valueOf((int) cursor.getFloat(1));

            int drawableId = GradeConverting.setGrade(tempGrade);
            grade.setImageResource(drawableId);

            String tempInformation = cursor.getString(2) + "개봉\n" + cursor.getString(3) + " / " + (int) cursor.getFloat(4) + "분";
            information.setText(tempInformation);

            String tempReservationRating = (int) cursor.getFloat(5) + "위 " + cursor.getFloat(6) + "%";
            reservationRating.setText(tempReservationRating);

            String tempAudienceRating = String.valueOf(cursor.getFloat(7));
            audienceRating.setText(tempAudienceRating);

            String tempAudience = String.valueOf((int) cursor.getFloat(8));
            audienceNum.setText(tempAudience);

            String tempStory = cursor.getString(9);
            story.setText(tempStory);

            String tempDirectorName = cursor.getString(10);
            directorName.setText(tempDirectorName);

            String tempCastName = cursor.getString(11);
            castName.setText(tempCastName);

            String tempLikeNum = Integer.toString((int) cursor.getFloat(12));
            tvLikeNum.setText(tempLikeNum);

            String tempUnlikeNum = Integer.toString((int) cursor.getFloat(13));
            tvUnlikeNum.setText(tempUnlikeNum);

            String tempImage = cursor.getString(14);
            Glide.with(mainActivity)
                    .load(tempImage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mainImage);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cursor.close();
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

        if (AppHelper.getDatabase() != null) {
            CommentTableName = "Comment" + movieId;

            AppHelper.createCommentTable("Comment", mainActivity, movieId);

            String sql = "insert or replace into " + CommentTableName + "(id, writer, movieId, writer_image, time, " +
                    "timestamp, rating, contents, recommend, total_count)" + " values(?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < sendedData.size(); i++) {
                Object[] params = {sendedData.get(i).getId(), sendedData.get(i).getWriter(), sendedData.get(i).getMovieId(), sendedData.get(i).getWriter_image()
                        , sendedData.get(i).getTime(), sendedData.get(i).getTimestamp(), sendedData.get(i).getRating()
                        , sendedData.get(i).getContents(), sendedData.get(i).getRecommend(), movieCommentList.getTotalCount()};

                AppHelper.getDatabase().execSQL(sql, params);
            }
        }

        listView = rootView.findViewById(R.id.lv_review);        // 어댑터를 사용할 리스트뷰를 설정한다.

        adapter = new CommentAdapter(mainActivity.getApplicationContext());        // 해당 리스트뷰를 위한 어댑터를 만든다.

        for (int i = 0; i < sendedData.size(); i++) {
            String tempWriter = sendedData.get(i).getWriter();
            String tempTime = sendedData.get(i).getTime();
            String tempContents = sendedData.get(i).getContents();
            String tempRating = String.valueOf(sendedData.get(i).getRating());
            adapter.addItem(new CommentItem(tempWriter, tempTime, tempContents, tempRating));
        }
        listView.setAdapter(adapter);
    }

    private void processDatabaseComment() {


        CommentTableName = "Comment" + movieId;

        String sql = "select writer, time, contents, rating from " + CommentTableName;        // 찾을 내용 수정
        Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();

        listView = rootView.findViewById(R.id.lv_review);        // 어댑터를 사용할 리스트뷰를 설정한다.

        adapter = new CommentAdapter(mainActivity.getApplicationContext());        // 해당 리스트뷰를 위한 어댑터를 만든다.

        try {
            for (int i = 0; i < cursor.getCount(); i++) {
                String tempWriter = cursor.getString(0);
                String tempTime = cursor.getString(1);
                String tempContents = cursor.getString(2);
                String tempRating = String.valueOf(cursor.getFloat(3));

                adapter.addItem(new CommentItem(tempWriter, tempTime, tempContents, tempRating));
                cursor.moveToNext();
            }

            listView.setAdapter(adapter);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        cursor.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1005 || requestCode == 1006) {
            mainActivity.onDetailView(movieId);
        }
    }


}