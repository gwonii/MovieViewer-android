package com.gmail.hc.gwnoii.movie.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gmail.hc.gwnoii.movie.R;
import com.gmail.hc.gwnoii.movie.model.Movie;

import java.util.HashMap;
import java.util.Map;

public class DoCommentActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private EditText commentText;
    private RatingBar ratingBar;
    private TextView title;
    private ImageView grade;

    private int movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_comment);

        Intent intent = getIntent();
        Movie passedData = intent.getParcelableExtra("movieData");

        saveButton = findViewById(R.id.btSave);
        cancelButton = findViewById(R.id.btCancel);
        commentText = findViewById(R.id.etDoComment);
        ratingBar = findViewById(R.id.rbDoEvaluate);
        title = findViewById(R.id.tvDoCommentMovieTitle);
        grade = findViewById(R.id.ivDoCommentAge);

        movieId = (int)passedData.getId();
        title.setText(passedData.getTitle());

        int drawableId = GradeConverting.setGrade(String.valueOf((int)passedData.getGrade()));
        grade.setImageResource(drawableId);

        commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentText.setText("");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String curNum = String.valueOf(movieId);

                String url = "http://" + AppHelper.host + ":" + AppHelper.port + "/movie/createComment";
                url += "?" + "id=" + curNum;

                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
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

                        params.put("writer", "gwonii");
                        String starValue = Float.toString(ratingBar.getRating());
                        params.put("rating", starValue);
                        params.put("contents", commentText.getText().toString());

                        return params;
                    }
                };

                request.setShouldCache(false);
                AppHelper.requestQueue.add(request);

                Intent resultIntent = new Intent();
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
