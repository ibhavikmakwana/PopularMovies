package com.ibhavikmakwana.popularmovies.movielist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private boolean isPopular = true;
    private PopularMovieListAdapter mPopularMovieListAdapter;
    private TopRatedMovieListAdapter mTopRatedMovieListAdapter;
    private List<Movies.Result> mPopularMoviesList;
    private List<Movies.Result> mTopMoviesList;
    private MovieViewModel mMovieViewModel;
    private Context mContext;
    private MoviesRepository mMoviesRepository;
    private int popularPage = 1;
    private int topRatedPage = 1;
    private int totalPopularPage;
    private int totalTopRatedPage;
    private GridLayoutManager mLayoutManager;

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading) {
                if (isPopular) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalPopularPage >= totalItemCount) {
                        popularPage++;
                        isLoading = true;
                        getPopularMovies(getResources().getString(R.string.api_key), popularPage);
                    }
                } else {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalTopRatedPage >= totalItemCount) {
                        topRatedPage++;
                        isLoading = true;
                        getTopRatedMovies(getResources().getString(R.string.api_key), topRatedPage);
                    }
                }
            }
        }
    };

    /**
     * Call this method to launch the activity.
     */
    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, MovieActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        mContext = MovieActivity.this;
        setToolbar(R.id.toolbar, getResources().getString(R.string.app_name), false);
        mToolbar.setOverflowIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_sort_24dp));
        mMoviesRepository = new MoviesRepository(RetrofitHelper.create());
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
        mTopMoviesList = new ArrayList<>();

        mPopularMovieListAdapter = new PopularMovieListAdapter(mPopularMoviesList, mContext);
        mTopRatedMovieListAdapter = new TopRatedMovieListAdapter(mTopMoviesList, mContext);

        mLayoutManager = new GridLayoutManager(mContext, 2);
        mRvMovies.setLayoutManager(mLayoutManager);
        mRvMovies.addOnScrollListener(recyclerViewOnScrollListener);
    }

    /**
     * Get the Popular movies from the server
     *
     * @param apiKey
     * @param page
     */
    private void getPopularMovies(String apiKey, final int page) {
        if (page == 1 && mPopularMoviesList.size() <= 0)
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
                isLoading = false;
                mViewFlipper.setDisplayedChild(1);
                if (page == 1) {
                    mPopularMoviesList.clear();
                    mPopularMoviesList.addAll(movies.getResults());
                    mRvMovies.setAdapter(mPopularMovieListAdapter);
                } else {
                    mPopularMoviesList.addAll(movies.getResults());
                }
                mPopularMovieListAdapter.setMovies(mPopularMoviesList);
                totalPopularPage = movies.getTotalPages();
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
                if (e instanceof UnknownHostException) {
                    if (mPopularMoviesList.size() > 0) {
                        mPopularMovieListAdapter.setMovies(mPopularMoviesList);
                    } else {
                        mViewFlipper.setDisplayedChild(2);
                        mIvError.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_signal_wifi_off_black_24dp));
                        mTvErrorMsg.setText(ERROR_MESSAGE_INTERNET_NOT_AVAILABLE);
                    }
                } else {
                    mViewFlipper.setDisplayedChild(2);
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
     * Get the top rated movies from the server
     *
     * @param apiKey
     * @param page
     */
    private void getTopRatedMovies(String apiKey, final int page) {
        if (page == 1 && mTopMoviesList.size() <= 0)
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
                isLoading = false;
                mViewFlipper.setDisplayedChild(1);
                if (page == 1) {
                    mTopMoviesList.clear();
                    mTopMoviesList.addAll(movies.getResults());
                    mRvMovies.setAdapter(mTopRatedMovieListAdapter);
                } else {
                    mTopMoviesList.addAll(movies.getResults());
                }
                mPopularMovieListAdapter.setMovies(mTopMoviesList);
                mTopRatedMovieListAdapter.setMovies(mTopMoviesList);
                totalTopRatedPage = movies.getTotalPages();
            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
                if (e instanceof UnknownHostException) {
                    if (mTopMoviesList.size() > 0) {
                        mPopularMovieListAdapter.setMovies(mTopMoviesList);
                    } else {
                        mViewFlipper.setDisplayedChild(2);
                        mIvError.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_signal_wifi_off_black_24dp));
                        mTvErrorMsg.setText(ERROR_MESSAGE_INTERNET_NOT_AVAILABLE);
                    }
                } else {
                    mIvError.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_warning_black_24dp));
                    mTvErrorMsg.setText(SOMETHING_WENT_WRONG);
                    mViewFlipper.setDisplayedChild(2);
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
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cc_error:
                sortAndReloadMovieList();
                break;
        }
    }

    private void sortAndReloadMovieList() {
        if (isPopular) {
            if (mPopularMoviesList.size() > 0) {
                mRvMovies.setAdapter(mPopularMovieListAdapter);
                mViewFlipper.setDisplayedChild(1);
            } else {
                getPopularMovies(getResources().getString(R.string.api_key), popularPage);
            }
        } else {
            if (mTopMoviesList.size() > 0) {
                mRvMovies.setAdapter(mTopRatedMovieListAdapter);
                mViewFlipper.setDisplayedChild(1);
            } else {
                getTopRatedMovies(getResources().getString(R.string.api_key), topRatedPage);
            }
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
                    sortAndReloadMovieList();
                }
                break;
            case R.id.action_sort_top_rated:
                if (isPopular) {
                    item.setChecked(true);
                    isPopular = false;
                    sortAndReloadMovieList();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}