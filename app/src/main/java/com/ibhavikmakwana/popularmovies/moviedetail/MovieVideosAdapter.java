package com.ibhavikmakwana.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ibhavikmakwana.popularmovies.R;
import com.ibhavikmakwana.popularmovies.base.BaseRecyclerViewAdapter;
import com.ibhavikmakwana.popularmovies.model.Video;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerInitListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieVideosAdapter extends BaseRecyclerViewAdapter<MovieVideosAdapter.ViewHolder> {
    private List<Video.Result> mVideoList;
    private Context mContext;

    MovieVideosAdapter(List<Video.Result> videoList, Context context) {
        super(videoList);
        mVideoList = videoList;
        mContext = context;
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @Override
    public void bindView(ViewHolder holder, int position) {
        Video.Result videos = mVideoList.get(position);
        holder.mTvVideoName.setText(videos.getName());
        final String key = videos.getKey();

        holder.mIvMovieVideoView.initialize(new YouTubePlayerInitListener() {
            @Override
            public void onInitSuccess(@NonNull final YouTubePlayer youTubePlayer) {
                youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        super.onReady();
                        youTubePlayer.loadVideo(key, 0);
                        youTubePlayer.pause();
                    }
                });
            }
        }, true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_video, parent, false));
    }

    private Video.Result getItem(int position) {
        return mVideoList.get(position);
    }

    public void setVideos(List<Video.Result> videos) {
        mVideoList = videos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_movie_video_view)
        YouTubePlayerView mIvMovieVideoView;
        @BindView(R.id.tv_video_name)
        TextView mTvVideoName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}