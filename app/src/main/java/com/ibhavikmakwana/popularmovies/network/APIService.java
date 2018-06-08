package com.ibhavikmakwana.popularmovies.network;

import com.ibhavikmakwana.popularmovies.model.Movies;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Bhavik Makwana on 7/29/2017.
 */
public interface APIService {

    @GET("movie/popular")
    Observable<Movies> getPopularMovies(@Query("api_key") String apiKey,
                                        @Query("page") int page);

    @GET("movie/top_rated")
    Observable<Movies> getTopRatedMovies(@Query("api_key") String apiKey,
                                         @Query("page") int page);

    @GET("movie/{movie_id}")
    Observable<Movies> getMovieDetail(@Query("api_key") String apiKey,
                                      @Path("movie_id") int movieId);
}