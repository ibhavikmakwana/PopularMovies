package com.ibhavikmakwana.popularmovies.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.repository.MoviesRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Bhavik Makwana on 30-05-2018.
 */
public class MovieViewModel extends ViewModel {
    MoviesRepository mMoviesRepository;

    public MovieViewModel(MoviesRepository moviesRepository) {
        this.mMoviesRepository = moviesRepository;
    }

    public Observable<Movies> getPopularMovies(String apiKey, int page) {
        return mMoviesRepository.getPopularMovies(apiKey, page).debounce(400, TimeUnit.MILLISECONDS);
    }

    public Observable<Movies> getTopRatedMovies(String apiKey, int page) {
        return mMoviesRepository.getTopRatedMovies(apiKey, page).debounce(400, TimeUnit.MILLISECONDS);
    }
}