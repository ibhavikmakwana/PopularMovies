package com.ibhavikmakwana.popularmovies.network;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ibhavikmakwana.popularmovies.utils.Constant.BASE_URL;

/**
 * Created by Bhavik Makwana on 06/02/2018/.
 * Build the client for api service.
 */

public class RetrofitHelper {
    public static APIService create() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIService.class);
    }
}