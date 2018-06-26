package com.ibhavikmakwana.popularmovies.repository;

import android.util.Log;

import com.ibhavikmakwana.popularmovies.model.Detail;
import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.model.Video;
import com.ibhavikmakwana.popularmovies.network.APIService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Bhavik Makwana on 30-05-2018.
 */
@SuppressWarnings("unchecked")
public class MoviesRepository {
    private APIService mAPIService;

    public MoviesRepository(APIService apiService) {
        this.mAPIService = apiService;
    }

    public Observable<Movies> getPopularMovies(String apiKey, int page) {
        return Observable.concatArray(getPopularMoviesFromApi(apiKey, page));
    }

    public Observable<Movies> getTopRatedMovies(String apiKey, int page) {
        return Observable.concatArray(getTopRatedMoviesFromApi(apiKey, page));
    }

    public Observable<Detail> getMoviesDetail(String apiKey, int id) {
        return Observable.concatArray(getMovieDetailFromApi(apiKey, id));
    }

    public Observable<Video> getMoviesVideos(String apiKey, int id) {
        return Observable.concatArray(getMovieVideoFromApi(apiKey, id));
    }

    private Observable<Movies> getPopularMoviesFromApi(String apiKey, int page) {
        return mAPIService.getPopularMovies(apiKey, page).doOnNext(new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) {
                Log.d("Success", "Dispatching " + movies + "characters from API...");
            }
        });
    }

    private Observable<Movies> getTopRatedMoviesFromApi(String apiKey, int page) {
        return mAPIService.getTopRatedMovies(apiKey, page).doOnNext(new Consumer<Movies>() {
            @Override
            public void accept(Movies movies) {
                Log.d("Success", "Dispatching " + movies + "characters from API...");
            }
        });
    }


    private Observable<Detail> getMovieDetailFromApi(String apiKey, int id) {
        return mAPIService.getMovieDetail(id, apiKey).doOnNext(new Consumer<Detail>() {
            @Override
            public void accept(Detail detail) {
                Log.d("Success", "Dispatching " + detail + "characters from API...");
            }
        });
    }

    private Observable<Video> getMovieVideoFromApi(String apiKey, int id) {
        return mAPIService.getMovieVideos(id, apiKey).doOnNext(new Consumer<Video>() {
            @Override
            public void accept(Video video) {
                Log.d("Success", "Dispatching " + video + "characters from API...");
            }
        });
    }
}
