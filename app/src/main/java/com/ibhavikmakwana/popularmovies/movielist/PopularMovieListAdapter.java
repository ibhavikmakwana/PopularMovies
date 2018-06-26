package com.ibhavikmakwana.popularmovies.movielist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ibhavikmakwana.popularmovies.R;
import com.ibhavikmakwana.popularmovies.base.BaseRecyclerViewAdapter;
import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.moviedetail.MovieDetailActivity;
import com.ibhavikmakwana.popularmovies.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularMovieListAdapter extends BaseRecyclerViewAdapter<PopularMovieListAdapter.ViewHolder> {
    private List<Movies.Result> mMoviesList;
    private Context mContext;

    PopularMovieListAdapter(List<Movies.Result> moviesList, Context context) {
        super(moviesList);
        mMoviesList = moviesList;
        mContext = context;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void bindView(ViewHolder holder, int position) {
        Movies.Result movies = mMoviesList.get(position);
        holder.mTvMovieName.setText(movies.getOriginalTitle());
        holder.mRbMovieRating.setRating((float) (movies.getVoteAverage() / 2));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder);
        requestOptions.error(R.drawable.placeholder);
        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(Constant.POSTER_BASE_URL + movies.getPosterPath()).into(holder.mIvMoviePoster);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false));
    }

    private Movies.Result getItem(int position) {
        return mMoviesList.get(position);
    }

    public void setMovies(List<Movies.Result> popularMovies) {
        mMoviesList = popularMovies;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_poster)
        AppCompatImageView mIvMoviePoster;
        @BindView(R.id.tv_movie_name)
        TextView mTvMovieName;
        @BindView(R.id.rb_movie_rating)
        RatingBar mRbMovieRating;
        @BindView(R.id.root_card_view)
        CardView mRootCardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRootCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int movieId = getItem(getLayoutPosition()).getId();
                    MovieDetailActivity.launchActivity(mContext, movieId, mIvMoviePoster);
                }
            });
        }
    }
}