package com.example.soham.popularmovies.utility;

import android.text.TextUtils;
import android.util.Log;

import com.example.soham.popularmovies.data.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MoviesJSONUtils {

    private static final String LOG_TAG = MoviesJSONUtils.class.getSimpleName();

    /**
     * This method extracts the details from the json string which is passed along
     *
     * @param jsonResponse response string passed after the network request
     **/
    public static List<Movies> getDetailsFromJSON(String jsonResponse) {

        Movies movies;
        //Array List to store the movies object.
        List<Movies> mMoviesList = new ArrayList<>();
        //Static variables for JSON retrieval
        final String RESULTS = "results";
        final String VOTE_AVERAGE = "vote_average";
        final String ORIGINAL_TITLE = "original_title";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        //Return early if the jsonResponse is empty
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        try {
            JSONObject baseMovieJSON = new JSONObject(jsonResponse);
            JSONArray resultsJSONArray = baseMovieJSON.getJSONArray(RESULTS);
            for (int i = 0; i < resultsJSONArray.length(); i++) {
                JSONObject movieDetailsObject = resultsJSONArray.getJSONObject(i);
                Double userRating = movieDetailsObject.getDouble(VOTE_AVERAGE);
                String originalTitle = movieDetailsObject.getString(ORIGINAL_TITLE);
                String poster = movieDetailsObject.getString(POSTER_PATH);
                String plot = movieDetailsObject.getString(OVERVIEW);
                String releaseDate = movieDetailsObject.getString(RELEASE_DATE);

                movies = new Movies(userRating, originalTitle, poster, plot, releaseDate);
                mMoviesList.add(movies);
            }
        } catch (JSONException e) {
            Log.v(LOG_TAG, "Problem parsing JSON response");
        }
        return mMoviesList;
    }
}
