package com.example.soham.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Movies data model class implement parcelable interface.
 **/
public final class Movies implements Parcelable {

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

    /**
     * Constructor for parcelable interface
     *
     * @param in Parcel object
     **/
    protected Movies(Parcel in) {
        this.mRating = (Double) in.readValue(Double.class.getClassLoader());
        this.mTitle = in.readString();
        this.mPoster = in.readString();
        this.mPlot = in.readString();
        this.mDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mRating);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPoster);
        dest.writeString(this.mPlot);
        dest.writeString(this.mDate);
    }

    public static final Parcelable.Creator<Movies> CREATOR = new Parcelable.Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel source) {
            return new Movies(source);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

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
