package com.ibhavikmakwana.popularmovies.app;

import android.app.Application;
import android.content.Context;

import com.ibhavikmakwana.popularmovies.network.APIService;
import com.ibhavikmakwana.popularmovies.network.RetrofitHelper;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Bhavik Makwana on 06-06-2018.
 */
public class AppController extends Application {
    private APIService mAPIService;
    private Scheduler mScheduler;

    private static AppController get(Context context) {
        return (AppController) context.getApplicationContext();
    }

    public static AppController create(Context context) {
        return AppController.get(context);
    }

    public APIService getAPIService() {
        if (mAPIService == null) {
            mAPIService = RetrofitHelper.create();
        }
        return mAPIService;
    }

    public void setAPIService(APIService apiService) {
        this.mAPIService = apiService;
    }

    public Scheduler subscribeScheduler() {
        if (mScheduler == null) {
            mScheduler = Schedulers.io();
        }
        return mScheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.mScheduler = scheduler;
    }
}