package com.ibhavikmakwana.popularmovies.network;

import com.ibhavikmakwana.popularmovies.model.Detail;
import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.model.Video;

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
    Observable<Detail> getMovieDetail(@Path("movie_id") int movieId,
                                      @Query("api_key") String apiKey);


    @GET("movie/{movie_id}/videos")
    Observable<Video> getMovieVideos(@Path("movie_id") int movieId,
                                     @Query("api_key") String apiKey);
}