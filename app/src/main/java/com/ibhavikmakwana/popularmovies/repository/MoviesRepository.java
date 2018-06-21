package com.ibhavikmakwana.popularmovies.repository;

import android.content.Context;
import android.util.Log;

import com.ibhavikmakwana.popularmovies.model.Detail;
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

    public Observable<Detail> getMoviesDetail(String apiKey, int id) {
        return Observable.concatArray(getMovieDetailFromApi(apiKey, id));
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
        return mAPIService.getMovieDetail(id,apiKey).doOnNext(new Consumer<Detail>() {
            @Override
            public void accept(Detail detail) {
                Log.d("Success", "Dispatching " + detail + "characters from API...");
            }
        });
    }
}
