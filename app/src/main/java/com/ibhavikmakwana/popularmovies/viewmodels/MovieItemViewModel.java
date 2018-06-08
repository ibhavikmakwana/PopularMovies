package com.ibhavikmakwana.popularmovies.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;

import com.ibhavikmakwana.popularmovies.model.Movies;

import java.util.List;

/**
 * Created by Bhavik Makwana on 08-06-2018.
 */
public class MovieItemViewModel extends BaseObservable {
    private Movies.Result mPopularMovies;
    private Context mContext;

    public MovieItemViewModel(Movies.Result popularMovies, Context context) {
        this.mPopularMovies = popularMovies;
        this.mContext = context;
    }

    public int getId() {
        return mPopularMovies.getId();
    }

    public boolean isVideo() {
        return mPopularMovies.getVideo();
    }

    public double getVoteAverage() {
        return mPopularMovies.getVoteAverage();
    }

    public String getTitle() {
        return mPopularMovies.getTitle();
    }

    public String getOriginalTitle() {
        return mPopularMovies.getOriginalTitle();
    }

    public Double getPopularity() {
        return mPopularMovies.getPopularity();
    }

    public String getPosterPath() {
        return mPopularMovies.getPosterPath();
    }

    public String getBackdropPath() {
        return mPopularMovies.getBackdropPath();
    }

    public String getOriginalLanguage() {
        return mPopularMovies.getOriginalLanguage();
    }

    public List<Integer> getGenreIds() {
        return mPopularMovies.getGenreIds();
    }

    public boolean isAdult() {
        return mPopularMovies.getAdult();
    }

    public String getOverView() {
        return mPopularMovies.getOverview();
    }

    public String getReleaseDate() {
        return mPopularMovies.getReleaseDate();
    }
}