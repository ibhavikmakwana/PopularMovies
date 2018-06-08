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
import com.bumptech.glide.load.model.GlideUrl;
import com.ibhavikmakwana.popularmovies.R;
import com.ibhavikmakwana.popularmovies.base.BaseRecyclerViewAdapter;
import com.ibhavikmakwana.popularmovies.model.Movies;
import com.ibhavikmakwana.popularmovies.moviedetail.MovieDetailActivity;
import com.ibhavikmakwana.popularmovies.utils.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PopularMovieAdapter extends BaseRecyclerViewAdapter<PopularMovieAdapter.ViewHolder> {
    private List<Movies.Result> mPopularMoviesList;
    private Context mContext;

    PopularMovieAdapter(List<Movies.Result> popularMoviesList, Context context) {
        super(popularMoviesList);
        mPopularMoviesList = popularMoviesList;
        mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(ViewHolder holder, int position) {
        Movies.Result movies = mPopularMoviesList.get(position);
        holder.mTvMovieName.setText(movies.getOriginalTitle());
        holder.mRbMovieRating.setRating((float) (movies.getVoteAverage() / 2));
        Glide.with(mContext).load(new GlideUrl(Constant.POSTER_BASE_URL + movies.getPosterPath())).into(holder.mIvMoviePoster);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_movie, parent, false));
    }

    private Movies.Result getItem(int position) {
        return mPopularMoviesList.get(position);
    }

    public void setMovies(List<Movies.Result> popularMovies) {
        mPopularMoviesList = popularMovies;
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
                    MovieDetailActivity.launchActivity(mContext, movieId);
                }
            });
        }
    }
}