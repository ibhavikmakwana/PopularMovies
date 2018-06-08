package com.ibhavikmakwana.popularmovies.repository;

import android.content.Context;
import android.util.Log;

import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.network.APIService;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Bhavik Makwana on 30-05-2018.
 */
@SuppressWarnings("unchecked")
public class MoviesRepository {
    private Context mContext;
    private APIService mAPIService;

    public MoviesRepository(APIService apiService, Context context) {
        this.mAPIService = apiService;
        this.mContext = context;
    }

    public Observable<Movies> getPopularMovies(String apiKey, int page) {
        return Observable.concatArray(getPopularMoviesFromApi(apiKey, page));
    }

    public Observable<Movies> getTopRatedMovies(String apiKey, int page) {
        return Observable.concatArray(getTopRatedMoviesFromApi(apiKey, page));
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
}
