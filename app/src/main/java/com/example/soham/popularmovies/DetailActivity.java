package com.example.soham.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.soham.popularmovies.utility.NetworkUtils.simpleDate;

public class DetailActivity extends AppCompatActivity {

    private String movieTitle;
    private Double movieRating;
    private String moviePLot;
    private String movieReleaseDate;
    private String moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mMovieTitle = findViewById(R.id.pm_movie_title);
        TextView mMoviePlot = findViewById(R.id.pm_movie_plot);
        TextView mMovieRating = findViewById(R.id.pm_movie_rating);
        TextView mReleaseDate = findViewById(R.id.pm_release_date);
        ImageView mMoviePoster = findViewById(R.id.pm_image_poster);

        Intent receivedMovieIntent = getIntent();
        ///Get details from the received intent
        if (receivedMovieIntent != null) {
            movieTitle = receivedMovieIntent.getStringExtra(String.valueOf(R.string.intent_movie_title));
            movieRating = receivedMovieIntent.getDoubleExtra(String.valueOf(R.string.intent_movie_rating), 0.0);
            moviePLot = receivedMovieIntent.getStringExtra(String.valueOf(R.string.intent_movie_plot));
            movieReleaseDate = receivedMovieIntent.getStringExtra(String.valueOf(R.string.intent_movie_release));
            moviePoster = receivedMovieIntent.getStringExtra(String.valueOf(R.string.intent_movie_poster));
        }

        Uri posterUri = Uri.parse(moviePoster);
        mMovieTitle.setText(movieTitle);
        mMovieRating.setText(String.valueOf(movieRating));
        mMoviePlot.setText(moviePLot);
        mReleaseDate.setText(simpleDate(movieReleaseDate));
        Picasso.with(this).load(posterUri).into(mMoviePoster);
    }
}
