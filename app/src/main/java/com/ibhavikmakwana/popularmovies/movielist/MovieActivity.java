package com.ibhavikmakwana.popularmovies.movielist;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.ibhavikmakwana.popularmovies.R;
import com.ibhavikmakwana.popularmovies.base.BaseActivity;
import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.network.RetrofitHelper;
import com.ibhavikmakwana.popularmovies.repository.MoviesRepository;
import com.ibhavikmakwana.popularmovies.viewmodels.MovieViewModel;

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

public class MovieActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = MovieActivity.class.getSimpleName();
    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;
    @BindView(R.id.activity_main_view_flipper)
    ViewFlipper mViewFlipper;
    @BindView(R.id.iv_error)
    ImageView mIvError;
    @BindView(R.id.tv_error_msg)
    TextView mTvErrorMsg;
    @BindView(R.id.cc_error)
    ConstraintLayout mCcError;
    boolean isLoading = false;
    private boolean isPopular = true;
    private PopularMovieAdapter mPopularMovieAdapter;
    private List<Movies.Result> mPopularMoviesList;
    private List<Movies.Result> mTopMoviesList;
    private MovieViewModel mMovieViewModel;
    private Context mContext;
    private MoviesRepository mMoviesRepository;
    private int popularPage = 1;
    private int topRatedPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mContext = MovieActivity.this;
        setToolbar(R.id.toolbar, getResources().getString(R.string.app_name), false);
        mMoviesRepository = new MoviesRepository(RetrofitHelper.create(), mContext);
        setUpPopularMovie();
        getPopularMovies(getResources().getString(R.string.api_key), popularPage);
        mCcError.setOnClickListener(this);
    }

    /**
     * Initialize the recycler view and adapter and list
     */
    private void setUpPopularMovie() {
        mMovieViewModel = new MovieViewModel(mMoviesRepository);
        mPopularMoviesList = new ArrayList<>();
        mPopularMovieAdapter = new PopularMovieAdapter(mPopularMoviesList, mContext);
        mRvMovies.setAdapter(mPopularMovieAdapter);
        mRvMovies.setLayoutManager(new GridLayoutManager(mContext, 2));
    }

    /**
     * Get the Popular movies from the server
     *
     * @param apiKey
     * @param page
     */
    private void getPopularMovies(String apiKey, int page) {
        mViewFlipper.setDisplayedChild(0);
        mMovieViewModel.getPopularMovies(apiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Movies>() {
            @Override
            public void onSubscribe(Disposable d) {
                addSubscription(d);
            }

            @Override
            public void onNext(Movies movies) {
                mViewFlipper.setDisplayedChild(1);
                mPopularMoviesList.addAll(movies.getResults());
                Log.d(TAG, mPopularMoviesList.toString());
                mPopularMovieAdapter.setMovies(mPopularMoviesList);
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

    /**
     * Click Listener for the views
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cc_error:
                if (isPopular)
                    getPopularMovies(getResources().getString(R.string.api_key), popularPage);
                else
                    getTopRatedMovies(getResources().getString(R.string.api_key), popularPage);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                if (!isPopular) {
                    item.setChecked(true);
                    isPopular = true;
                    getPopularMovies(getResources().getString(R.string.api_key), popularPage);
                }
                break;
            case R.id.action_sort_top_rated:
                if (isPopular) {
                    item.setChecked(true);
                    isPopular = false;
                    getTopRatedMovies(getResources().getString(R.string.api_key), topRatedPage);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * Get the top rated movies from the server
     *
     * @param apiKey
     * @param page
     */
    private void getTopRatedMovies(String apiKey, int page) {
        mViewFlipper.setDisplayedChild(0);
        mMovieViewModel.getTopRatedMovies(apiKey, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Movies>() {
            @Override
            public void onSubscribe(Disposable d) {
                addSubscription(d);
            }

            @Override
            public void onNext(Movies movies) {
                mViewFlipper.setDisplayedChild(1);
                mPopularMoviesList.addAll(movies.getResults());
                Log.d(TAG, mPopularMoviesList.toString());
                mPopularMovieAdapter.setMovies(mPopularMoviesList);
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
}