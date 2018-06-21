package com.ibhavikmakwana.popularmovies;

import android.os.Bundle;
import android.os.Handler;

import com.ibhavikmakwana.popularmovies.base.BaseActivity;
import com.ibhavikmakwana.popularmovies.base.RippleBackground;
import com.ibhavikmakwana.popularmovies.movielist.MovieActivity;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.content)
    RippleBackground mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContent.startRippleAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MovieActivity.launchActivity(SplashActivity.this);
                finish();
            }

        }, 6000); // wait for 5 seconds
    }
}
