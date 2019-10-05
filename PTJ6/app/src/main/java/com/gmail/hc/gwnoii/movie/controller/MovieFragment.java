package com.gmail.hc.gwnoii.movie.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gmail.hc.gwnoii.movie.R;
import com.gmail.hc.gwnoii.movie.model.Movie;

public class MovieFragment extends Fragment {

    private FragmentCallback callback;
    private Movie movie;
    private MainActivity mainActivity;

    public MovieFragment(Movie movie) {
        this.movie = movie;                 // 뷰페이저에서 요청한 api의 데이터가 Movie형태로 전달됨
                                            // 이걸 가지고 프래그먼트를 그려주면 된다.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mainActivity = (MainActivity)getActivity();

        if (context instanceof FragmentCallback) {
            callback = (FragmentCallback) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
        if (callback != null) {
            callback = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.content_list_view, container, false);

        ImageView mainImage = rootView.findViewById(R.id.ivMainImage);
        TextView title = rootView.findViewById(R.id.tvTitle);
        TextView reservationRate = rootView.findViewById(R.id.tvReservationRate);
        TextView grade = rootView.findViewById(R.id.tvGrade);

        String tempTitle = (int) movie.getId() + ". " + movie.getTitle();
        title.setText(tempTitle);
        String tempReservationRate = (movie.getReservation_rate()) + "% | ";
        reservationRate.setText(tempReservationRate);
        String tempGrade = ((int) movie.getGrade()) + "세 관람가";
        grade.setText(tempGrade);

        Glide.with(mainActivity)
                .load(movie.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mainImage);


        Button detailView = rootView.findViewById(R.id.btDetail);
        detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onDetailView((int) movie.getId());             // getId는 이미 전달됨 ViewPagerFragment에서
                }
            }
        });

        return rootView;
    }
}
