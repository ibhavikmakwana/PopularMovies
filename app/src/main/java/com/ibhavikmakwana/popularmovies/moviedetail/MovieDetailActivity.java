package com.ibhavikmakwana.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.ibhavikmakwana.popularmovies.model.Video;
import com.ibhavikmakwana.popularmovies.network.RetrofitHelper;
import com.ibhavikmakwana.popularmovies.repository.MoviesRepository;
import com.ibhavikmakwana.popularmovies.utils.Constant;
import com.ibhavikmakwana.popularmovies.viewmodels.MovieDetailViewModel;

import org.apache.commons.lang3.LocaleUtils;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ibhavikmakwana.popularmovies.utils.Constant.ERROR_MESSAGE_INTERNET_NOT_AVAILABLE;
import static com.ibhavikmakwana.popularmovies.utils.Constant.SOMETHING_WENT_WRONG;

public class MovieDetailActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.tv_label_videos)
    TextView mTvLabelVideos;
    @BindView(R.id.rv_movie_video)
    RecyclerView mRvMovieVideo;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error_msg)
    TextView mTvErrorMsg;
    @BindView(R.id.cc_error)
    ConstraintLayout mCcError;
    private MovieDetailViewModel mMovieDetailViewModel;
    private Context mContext;
    private RequestOptions requestOptions;
    private int movieId;

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
        setToolbar(R.id.collapsing_toolbar, "", true);
        movieId = getIntent().getIntExtra(MOVIE_ID, 0);
        if (movieId == 0) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            finish();
        }
        MoviesRepository moviesRepository = new MoviesRepository(RetrofitHelper.create());
        mMovieDetailViewModel = new MovieDetailViewModel(moviesRepository);
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);
        getMovieDetail(getResources().getString(R.string.api_key), movieId);
        getMovieVideos(getResources().getString(R.string.api_key), movieId);
        mCcError.setOnClickListener(this);
        mRvMovieVideo.setNestedScrollingEnabled(false);
    }

    private void getMovieVideos(String apiKey, int movieId) {
        mMovieDetailViewModel.getMoviesVideos(apiKey, movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Video>() {
            @Override
            public void onSubscribe(Disposable d) {
                addSubscription(d);
            }

            @Override
            public void onNext(Video video) {
                if (video.getResults().size() > 0) {
                    loadVideos(video.getResults());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "Completed");
            }
        });
    }

    /**
     * @param results
     */
    private void loadVideos(List<Video.Result> results) {
        mTvLabelVideos.setVisibility(View.VISIBLE);
        mRvMovieVideo.setVisibility(View.VISIBLE);
        List<Video.Result> videoResult = new ArrayList<>(results);
        MovieVideosAdapter movieVideosAdapter = new MovieVideosAdapter(videoResult, mContext);
        mRvMovieVideo.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        mRvMovieVideo.setAdapter(movieVideosAdapter);
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
                    mIvError.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_signal_wifi_off_black_24dp));
                    mTvErrorMsg.setText(ERROR_MESSAGE_INTERNET_NOT_AVAILABLE);
                } else {
                    mIvError.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_warning_black_24dp));
                    mTvErrorMsg.setText(SOMETHING_WENT_WRONG);
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

    /**
     * Click Listener for the views
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cc_error:
                getMovieDetail(getResources().getString(R.string.api_key), movieId);
                getMovieVideos(getResources().getString(R.string.api_key), movieId);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
