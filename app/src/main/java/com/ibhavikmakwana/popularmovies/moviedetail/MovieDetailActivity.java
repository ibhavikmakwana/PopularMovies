package com.ibhavikmakwana.popularmovies.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ibhavikmakwana.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_ID = "MOVIE_ID";
    private int movieId;

    /**
     * Call this method to launch the activity.
     */
    public static void launchActivity(Context context, int movieId) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieId = getIntent().getIntExtra(MOVIE_ID, 0);
        if (movieId == 0) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
