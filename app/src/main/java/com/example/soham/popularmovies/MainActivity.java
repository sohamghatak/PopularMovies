package com.example.soham.popularmovies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.soham.popularmovies.data.MoviePreferences;
import com.example.soham.popularmovies.data.Movies;
import com.example.soham.popularmovies.utility.MoviesJSONUtils;
import com.example.soham.popularmovies.utility.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    //private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    //private TextView mErrorMessage;
    //private ProgressBar mLoadingIndicator;
    //Local variable to store the menu item id clicked
    private int itemId;
    private boolean connection;
    //Using ButterKnife to bind views
    @BindView(R.id.pm_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pm_error_message)
    TextView mErrorMessage;
    @BindView(R.id.pm_loading_indicator)
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        //GridLayoutManager to populate a grid layout
        GridLayoutManager gridLayoutManager = new GridLayoutManager
                (this, getColumnSpan());
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);
        //Below condition checks the saved instance state
        if (savedInstanceState == null) {
            nowPlayingMovies();
        } else {
            getUserSettings(savedInstanceState.getInt("selectedOption", R.id.now_playing_settings));
        }

    }

    /**
     * Save the instance using the below method and
     * storing the movie preference selected in the menu item
     **/
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("selectedOption", itemId);
    }

    @Override
    public void onClick(Movies movies) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(String.valueOf(R.string.intent_movies), movies);
        startActivity(intentToStartDetailActivity);
    }

    /**
     * AsyncTask class to handle network operation in background thread
     **/
    @SuppressLint("StaticFieldLeak")
    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movies>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movies> doInBackground(String... strings) {
            String response = null;
            List<Movies> mMoviesList;
            URL url = NetworkUtils.buildURL(strings[0]);
            try {
                response = NetworkUtils.getHTTPResponse(url);
            } catch (IOException e) {
                Log.v(LOG_TAG, "Problem in receiving response from HTTP request");
            }

            mMoviesList = MoviesJSONUtils.getDetailsFromJSON(response);

            return mMoviesList;
        }

        @Override
        protected void onPostExecute(List<Movies> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesData();
                mMovieAdapter.addListMovies(movies);
            } else {
                mErrorMessage.setText(R.string.error_message);
                showErrorMessage();
            }
        }
    }

    /**
     * Method to get a list of now playing movies
     **/
    private void nowPlayingMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        String nowPlaying = MoviePreferences.getNowPlaying();
        connection = checkConnection();
        //Check if internet connection is active.
        if (connection) {
            fetchMoviesTask.execute(nowPlaying);
        } else {
            noInternetErrorMessage();
        }
    }

    /**
     * Method to get a list of popular movies
     **/
    private void mostPopularMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        String mostPopular = MoviePreferences.getMostPopular();
        connection = checkConnection();
        //Check if internet connection is active.
        if (connection) {
            fetchMoviesTask.execute(mostPopular);
        } else {
            noInternetErrorMessage();
        }
    }

    /**
     * Method to get a list of top rated movies
     **/
    private void highRatedMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        String topRated = MoviePreferences.getTopRated();
        connection = checkConnection();
        //Check if internet connection is active.
        if (connection) {
            fetchMoviesTask.execute(topRated);
        } else {
            noInternetErrorMessage();
        }
    }

    /**
     * Inflate menu options
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Call a method to get perform action based on menu item selected.
        getUserSettings(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method to execute task based on User clicked menu options.
     **/
    private void getUserSettings(int selectedSetting) {
        itemId = selectedSetting;
        switch (itemId) {
            case R.id.now_playing_settings:
                nowPlayingMovies();
                break;
            case R.id.most_popular_settings:
                mostPopularMovies();
                break;
            case R.id.high_rated_settings:
                highRatedMovies();
                break;
            case R.id.refresh:
                mErrorMessage.setText("");
                nowPlayingMovies();
                break;
        }
    }

    /**
     * This method shows the movies data and hides the error message
     **/
    private void showMoviesData() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method shows the error message if the movies fail to load
     **/
    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    /**
     * This method shows the error message if there is no internet connection
     **/
    private void noInternetErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
        mErrorMessage.setText(R.string.error_message_no_internet);
    }

    /**
     * This method returns the number of columns based on the screen orientation.
     **/
    private int getColumnSpan() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return 4;
        }
        return 2;
    }

    /**
     * Helper method to check if internet connection is active on the device.
     **/
    private boolean checkConnection() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
