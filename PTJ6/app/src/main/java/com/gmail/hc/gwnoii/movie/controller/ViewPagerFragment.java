package com.gmail.hc.gwnoii.movie.controller;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.hc.gwnoii.movie.R;
import com.gmail.hc.gwnoii.movie.model.Movie;
import com.gmail.hc.gwnoii.movie.model.MovieList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPagerFragment extends Fragment {

    private MainActivity mainActivity;
    private MoviePagerAdapter pagerAdapter;
    private ViewPager viewPager;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_pager, container, false);

        viewPager = rootView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(5);

        pagerAdapter = new MoviePagerAdapter(mainActivity.getSupportFragmentManager());

        if (NetworkStatus.getConnectivityStatus(mainActivity) == NetworkStatus.TYPE_NOT_CONNECTED) {
            processDatabase();
        } else {
            sendRequest();
        }
        return rootView;
    }

    class MoviePagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        private MoviePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private void addItem(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private void sendRequest() {

        String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/readMovieList";
        url += "?" + "type=1";

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

        // 인터넷이 연결되어 있을 경우 웹서버의 API에 요청하여 데이터를 받는다.
        Gson gson = new Gson();
        MovieList movieList = gson.fromJson(response, MovieList.class);

        ArrayList<Movie> movies = movieList.getResult();

        // 인터넷이 연결되어 있는 동안에 데이터베이스에 데이터를 저장시킨다.
        if (AppHelper.getDatabase() != null) {

            AppHelper.createMovieTable("Movie", mainActivity);                                                                        // Movie table 생성

            String sql = "insert or replace into " + "Movie" + "(id, title, title_eng, date, user_rating, audience_rating, " +          // table에 데이터 삽입
                    "reviewer_rating, reservation_rate, reservation_grade, grade, thumb, image)" + " values(?,?,?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < movies.size(); i++) {

                Object[] params = {movies.get(i).getId(), movies.get(i).getTitle(), movies.get(i).getTitle_eng(), movies.get(i).getDate()
                        , movies.get(i).getUser_rating(), movies.get(i).getAudience_rating(), movies.get(i).getReviewer_rating(), movies.get(i).getReservation_rate()
                        , movies.get(i).getReservation_grade(), movies.get(i).getGrade(), movies.get(i).getThumb(), movies.get(i).getImage()};

                AppHelper.getDatabase().execSQL(sql, params);
            }
        }

        for (int i = 0; i < movies.size(); i++) {
            pagerAdapter.addItem(new MovieFragment(movies.get(i)));         // Moviefragment 생성할 때 매개변수로 Movie를 전달
        }
        viewPager.setAdapter(pagerAdapter);
    }

    private void processDatabase() {
        // 인터넷이 연결되어 있지 않은 경우 저장되어 있는 데이터베이스에서 데이터를 가져와 사용한다.

        String sql = "select id, title, reservation_rate, grade, image from " + "Movie";        // 찾을 내용 수정
        Cursor cursor = AppHelper.getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {

            Movie movie = new Movie();

            float id = cursor.getFloat(0);
            movie.setId(id);

            String title = cursor.getString(1);
            movie.setTitle(title);

            float reservation_rate = cursor.getFloat(2);
            movie.setReservation_rate(reservation_rate);

            float grade = cursor.getFloat(3);
            movie.setGrade(grade);

            String image = cursor.getString(4);
            movie.setImage(image);

            cursor.moveToNext();

            pagerAdapter.addItem(new MovieFragment(movie));
        }

        cursor.close();
        viewPager.setAdapter(pagerAdapter);
    }
}