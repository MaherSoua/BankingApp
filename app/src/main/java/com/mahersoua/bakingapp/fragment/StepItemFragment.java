package com.mahersoua.bakingapp.fragment;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class StepItemFragment extends Fragment {

    private static final String TAG = "StepItemFragment";
    private StepModel mStepModel;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private View mView;
    private long currentPositionPlayer;
    private int currentWindowIndex;
    private boolean playWhenReady;
    private IStepItemFragment mListener;
    private int mId;
    private Dialog mFullScreenDialog;
    private boolean isFullScreen;
    private int mCurrentItem;


    public void setStepModel(StepModel stepModel, int id, int currentItem) {
        mStepModel = stepModel;
        mId = id;
        mCurrentItem = currentItem;
    }

    public void setListener(IStepItemFragment listener){
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (mStepModel == null && savedInstanceState != null) {
            mStepModel = savedInstanceState.getParcelable("step-model");
            mCurrentItem = savedInstanceState.getInt("current-item");
            mId = savedInstanceState.getInt("id");
        }

        mView = inflater.inflate(R.layout.fragment_step_item, container, false);
        TextView descriptionTv = mView.findViewById(R.id.stepDescription);
        descriptionTv.setText(mStepModel.getDescription());
        mPlayerView = mView.findViewById(R.id.video_view);
        initializePlayer();
        initFullscreenDialog();
        ViewPager vp=(ViewPager) getActivity().findViewById(R.id.pager);

        mCurrentItem = (vp != null) ? vp.getCurrentItem() : mCurrentItem;
        if( mCurrentItem == mId){
            updatePlayerDisplay();
        }
        if(mListener != null){
            mListener.onViewLoaded(mPlayerView, mId);
        }
        return mView;
    }

    private void updatePlayerDisplay( ){
        if (!getContext().getResources().getBoolean(R.bool.isTablet)) {
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                closeFullscreenDialog();
            } else {
                openFullscreenDialog();
            }
        }
    }

    private void initFullscreenDialog() {
        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (isFullScreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.setContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        isFullScreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        if(mPlayerView == null) return;
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((FrameLayout) mView.findViewById(R.id.playerContainer)).addView(mPlayerView);
        isFullScreen = false;
        mFullScreenDialog.dismiss();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("step-model", mStepModel);
        outState.putInt("current-item", mCurrentItem);
        outState.putInt("id", mId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            currentPositionPlayer = mPlayer.getCurrentPosition();
            currentWindowIndex = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer = null;
        }
    }

    private void initializePlayer() {
        mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        Uri uri = Uri.parse(mStepModel.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "BankingApp"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }

    public interface IStepItemFragment {
        void onViewLoaded(PlayerView playerView, int id);
    }
}
