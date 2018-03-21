package com.example.soham.popularmovies.data;

/**
 * Movies data model class
 **/
public final class Movies {

    private final Double mRating;
    private final String mTitle;
    private final String mPoster;
    private final String mPlot;
    private final String mDate;
    //Static movie poster URL
    private final static String MOVIES_POSTER_URL = "http://image.tmdb.org/t/p/w185";

    public Movies(double rating, String title, String poster, String plot, String date) {
        mRating = rating;
        mTitle = title;
        mPoster = poster;
        mPlot = plot;
        mDate = date;
    }

    public String getMovieTitle() {
        return mTitle;
    }

    public Double getMovieRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mDate;
    }

    public String getMoviePlot() {
        return mPlot;
    }

    public String getMoviePoster() {
        return MOVIES_POSTER_URL + mPoster;
    }
}
