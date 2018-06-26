package com.ibhavikmakwana.popularmovies.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.ibhavikmakwana.popularmovies.model.Detail;
import com.ibhavikmakwana.popularmovies.model.Video;
import com.ibhavikmakwana.popularmovies.repository.MoviesRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by Bhavik Makwana on 30-05-2018.
 */
public class MovieDetailViewModel extends ViewModel {
    MoviesRepository mMoviesRepository;

    public MovieDetailViewModel(MoviesRepository moviesRepository) {
        this.mMoviesRepository = moviesRepository;
    }

    public Observable<Detail> getMoviesDetail(String apiKey, int id) {
        return mMoviesRepository.getMoviesDetail(apiKey, id).debounce(400, TimeUnit.MILLISECONDS);
    }

    public Observable<Video> getMoviesVideos(String apiKey, int id) {
        return mMoviesRepository.getMoviesVideos(apiKey, id).debounce(400, TimeUnit.MILLISECONDS);
    }
}