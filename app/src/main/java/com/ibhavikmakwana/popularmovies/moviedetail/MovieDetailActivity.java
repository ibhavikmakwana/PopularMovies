package com.ibhavikmakwana.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ibhavikmakwana.popularmovies.R;
import com.ibhavikmakwana.popularmovies.base.BaseActivity;
import com.ibhavikmakwana.popularmovies.model.Detail;
import com.ibhavikmakwana.popularmovies.network.RetrofitHelper;
import com.ibhavikmakwana.popularmovies.repository.MoviesRepository;
import com.ibhavikmakwana.popularmovies.utils.Constant;
import com.ibhavikmakwana.popularmovies.viewmodels.MovieDetailViewModel;

import org.apache.commons.lang3.LocaleUtils;

import java.net.UnknownHostException;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends BaseActivity {

    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static final String MOVIE_ID = "MOVIE_ID";
    @BindView(R.id.iv_movie_image)
    AppCompatImageView mIvMovieImage;
    @BindView(R.id.tv_movie_detail_title)
    TextView mTvMovieDetailTitle;
    @BindView(R.id.tv_movie_detail_tag_line)
    TextView mTvMovieDetailTagLine;
    @BindView(R.id.tv_movie_detail_language)
    TextView mTvMovieDetailLanguage;
    @BindView(R.id.tv_movie_released_on_label)
    TextView mTvMovieReleasedOnLabel;
    @BindView(R.id.tv_movie_released_date)
    TextView mTvMovieReleasedDate;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.tv_overview_label)
    TextView mTvOverviewLabel;
    @BindView(R.id.tv_movie_detail_overview)
    TextView mTvMovieDetailOverview;
    @BindView(R.id.view_flipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.ll_genre_chip_container)
    LinearLayout mLlGenreChipContainer;
    private MovieDetailViewModel mMovieDetailViewModel;
    private MoviesRepository mMoviesRepository;
    private Context mContext;
    private RequestOptions requestOptions;

    /**
     * Call this method to launch the activity.
     */
    public static void launchActivity(Context context, int movieId, View ivMoviePoster) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MOVIE_ID, movieId);

        String posterKey = context.getString(R.string.poster_image);
        ActivityOptions transitionActivityOptions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, ivMoviePoster, posterKey);
            context.startActivity(intent, transitionActivityOptions.toBundle());
        } else {
            context.startActivity(intent);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        mContext = MovieDetailActivity.this;
        int movieId = getIntent().getIntExtra(MOVIE_ID, 0);
        if (movieId == 0) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
        mMoviesRepository = new MoviesRepository(RetrofitHelper.create(), mContext);
        mMovieDetailViewModel = new MovieDetailViewModel(mMoviesRepository);
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);
        getMovieDetail(getResources().getString(R.string.api_key), movieId);
    }


    private void getMovieDetail(String apiKey, int movieId) {
        mViewFlipper.setDisplayedChild(0);
        mMovieDetailViewModel.getMoviesDetail(apiKey, movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Detail>() {
            @Override
            public void onSubscribe(Disposable d) {
                addSubscription(d);
            }

            @Override
            public void onNext(Detail detail) {
                mViewFlipper.setDisplayedChild(1);
                setUpUI(detail);
            }

            @Override
            public void onError(Throwable e) {
                mViewFlipper.setDisplayedChild(2);
                if (e instanceof UnknownHostException) {

                } else {

                }
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "Completed");
            }
        });
    }

    private void setUpUI(Detail detail) {
        //Image
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(Constant.POSTER_ORIGINAL_URL + detail.getPosterPath()).into(mIvMovieImage);
        mTvMovieDetailTitle.setText(detail.getOriginalTitle());
        mTvMovieDetailTagLine.setText(detail.getTagline());
        mTvMovieDetailLanguage.setText(LocaleUtils.toLocale(detail.getOriginalLanguage()).getDisplayLanguage());
        mTvMovieReleasedDate.setText(detail.getReleaseDate());
        mTvMovieDetailOverview.setText(detail.getOverview());
        mRatingBar.setRating((float) (detail.getVoteAverage() / 2));

        for (int i = 0; i < detail.getGenres().size(); i++) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.genre_layout, null);
            ((TextView) view.findViewById(R.id.tv_genre)).setText(detail.getGenres().get(i).getName());
            mLlGenreChipContainer.addView(view);
        }
    }
}
