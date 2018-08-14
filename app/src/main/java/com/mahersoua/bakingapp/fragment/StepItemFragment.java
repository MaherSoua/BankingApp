package com.mahersoua.bakingapp.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;

public class StepItemFragment extends Fragment {

    private static final String TAG = "StepItemFragment";
    private StepModel mStepModel;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private View mView;
    long currentPositionPlayer;
    int currentWindowIndex;
    boolean playWhenReady;

    public void setStepModel(StepModel stepModel){
        mStepModel = stepModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if(mStepModel == null && savedInstanceState != null){
            mStepModel = new StepModel();
            mStepModel.setVideoURL(savedInstanceState.getString("video-url"));
            mStepModel.setThumbnailURL(savedInstanceState.getString("thumbnail-url"));
            mStepModel.setDescription(savedInstanceState.getString("description"));
        }

        mView =  inflater.inflate(R.layout.step_item_fragment , container, false);
        TextView descriptionTv = mView.findViewById(R.id.stepDescription);
        descriptionTv.setText(mStepModel.getDescription());

        initializePlayer();

        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("description", mStepModel.getDescription());
        outState.putString("video-url", mStepModel.getVideoURL());
        outState.putString("thumbnail-url", mStepModel.getThumbnailURL());
        outState.putInt("step-id", mStepModel.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer(){
        if(mPlayer != null){
            mPlayer.release();
            currentPositionPlayer = mPlayer.getCurrentPosition();
            currentWindowIndex = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer = null;
        }
    }

    private void initializePlayer() {
        mPlayerView = mView.findViewById(R.id.video_view);
        mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector() , new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        Uri uri = Uri.parse(mStepModel.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri){
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "BankingApp"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }
}
