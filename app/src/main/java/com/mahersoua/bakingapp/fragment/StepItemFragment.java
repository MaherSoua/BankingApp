package com.mahersoua.bakingapp.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
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
import com.mahersoua.bakingapp.dataproviders.DataProvider;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.bakingapp.R;

public class StepItemFragment extends Fragment {
    public static final String STEP_MODEL = "step-model";
    public static final String STEP_ID = "step-id";
    public static final String STEP_MOVIE_URL = "step-movie-url";
    public static final String CURRENT_POSITION_PLAYER = "current-position-player";
    private static final String PLAY_WHEN_READY = "PLAY_WHEN_READY";

    private StepModel mStepModel;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private long currentPositionPlayer;
    private boolean isCurrentVisible;
    private int mId;
    private SimpleExoPlayer.DefaultEventListener mPlayerListener;
    private ImageView noImageView;

    public static StepItemFragment newInstance(StepModel stepModel, int id, int currentItem) {
        StepItemFragment fragment = new StepItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(STEP_MODEL, stepModel);
        args.putInt(STEP_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mStepModel = savedInstanceState.getParcelable(STEP_MODEL);
            mId = savedInstanceState.getInt(STEP_ID);
            currentPositionPlayer = savedInstanceState.getLong(CURRENT_POSITION_PLAYER);
        } else {
            mStepModel = getArguments().getParcelable(STEP_MODEL);
            mId = getArguments().getInt(STEP_ID);
        }
        int mCurrentItem = DataProvider.getCurrentStep();
        initListner();
        View mView = inflater.inflate(R.layout.fragment_step_item, container, false);
        mPlayerView = mView.findViewById(R.id.video_view);
        noImageView = mView.findViewById(R.id.noVideoImage);
        TextView descriptionTv = mView.findViewById(R.id.stepDescription);
        if (descriptionTv != null) {
            descriptionTv.setText(mStepModel.getDescription());
        }

        initializePlayer();

        if (mCurrentItem == mId) {
            updatePlayerDisplay();
        }
        return mView;
    }

    private void updatePlayerDisplay() {
        if (!getContext().getResources().getBoolean(R.bool.isTablet)) {
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_MODEL, mStepModel);
        outState.putInt(STEP_ID, mId);
        if (mPlayer != null) {
            outState.putLong(CURRENT_POSITION_PLAYER, mPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayer != null && isCurrentVisible) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayer != null && isCurrentVisible) {
            mPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
        }
    }

    private void initListner() {
        if (mPlayerListener == null) {
            mPlayerListener = new SimpleExoPlayer.DefaultEventListener() {
                boolean isPlayerError;

                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                    Toast.makeText(getContext(), "An error has occured " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    if (noImageView != null) {
                        noImageView.setVisibility(View.VISIBLE);
                    }
                    isPlayerError = true;
                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_READY) {
                        if (isPlayerError) {
                            mPlayerView.setVisibility(View.INVISIBLE);
                            if (noImageView != null) {
                                noImageView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mPlayerView.setVisibility(View.VISIBLE);
                            if (noImageView != null) {
                                noImageView.setVisibility(View.INVISIBLE);
                            }
                        }
                        Toast.makeText(getContext(), "Player is ready ", Toast.LENGTH_SHORT).show();
                    }
                }
            };
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer.setPlayWhenReady(false);
            mPlayer.removeListener(mPlayerListener);
            currentPositionPlayer = mPlayer.getCurrentPosition();
            mPlayer = null;
        }
    }

    private void initializePlayer() {
        if (mPlayerListener == null) return;
        releasePlayer();
        mPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        mPlayerView.setPlayer(mPlayer);
        mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        if (noImageView != null) {
            noImageView.setVisibility(View.INVISIBLE);
        }
        Uri uri = Uri.parse(mStepModel.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
        mPlayer.seekTo(currentPositionPlayer);
        mPlayer.addListener(mPlayerListener);
        mPlayer.setPlayWhenReady(false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "BankingApp"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCurrentVisible = isVisibleToUser;
        if (!isVisibleToUser) {
            releasePlayer();
        } else {
            if (mPlayer != null) {
                mPlayer.setPlayWhenReady(true);
            }
        }
    }
}