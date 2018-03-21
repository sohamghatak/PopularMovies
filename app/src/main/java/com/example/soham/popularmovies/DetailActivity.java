package com.example.soham.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soham.popularmovies.data.Movies;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.soham.popularmovies.utility.NetworkUtils.simpleDate;

public class DetailActivity extends AppCompatActivity {

    private String movieTitle;
    private Double movieRating;
    private String moviePLot;
    private String movieReleaseDate;
    private String moviePoster;
    //Using ButterKnife to bind views
    @BindView(R.id.pm_movie_title)
    TextView mMovieTitle;
    @BindView(R.id.pm_movie_plot)
    TextView mMoviePlot;
    @BindView(R.id.pm_movie_rating)
    TextView mMovieRating;
    @BindView(R.id.pm_release_date)
    TextView mReleaseDate;
    @BindView(R.id.pm_image_poster)
    ImageView mMoviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent receivedMovieIntent = getIntent();
        ///Get details from the received intent
        if (receivedMovieIntent != null) {
            Movies movies = receivedMovieIntent.getParcelableExtra(String.valueOf(R.string.intent_movies));
            movieTitle = movies.getMovieTitle();
            movieRating = movies.getMovieRating();
            moviePLot = movies.getMoviePlot();
            movieReleaseDate = movies.getReleaseDate();
            moviePoster = movies.getMoviePoster();
        }

        Uri posterUri = Uri.parse(moviePoster);
        mMovieTitle.setText(movieTitle);
        mMovieRating.setText(String.valueOf(movieRating));
        mMoviePlot.setText(moviePLot);
        mReleaseDate.setText(simpleDate(movieReleaseDate));
        Picasso.with(this)
                .load(posterUri)
                .placeholder(R.drawable.ic_movie_white_48dp)
                .error(R.drawable.ic_error_outline_white_48dp)
                .into(mMoviePoster);
    }
}
