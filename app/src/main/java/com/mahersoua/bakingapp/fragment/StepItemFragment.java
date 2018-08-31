package com.mahersoua.bakingapp.fragment;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mahersoua.bakingapp.activities.FullScreenVideoActivity;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.Observable;
import java.util.Observer;

public class StepItemFragment extends Fragment implements StepDetailsFragment.IStepDetails, Observer{
    public static final String STEP_MODEL = "step-model";
    public static final String CURRENT_ITEM = "current-item";
    public static final String STEP_ID = "step-id";

    private StepModel mStepModel;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private View mView;
    private long currentPositionPlayer;
    private int currentWindowIndex;
    private boolean playWhenReady;
    private int mId;
    private int mCurrentItem;
    private SimpleExoPlayer.DefaultEventListener mPlayerListener;
    private ImageView noImageView;

    public static StepItemFragment newInstance(StepModel stepModel, int id, int currentItem) {
        StepItemFragment fragment = new StepItemFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_ITEM, currentItem);
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
            mCurrentItem = savedInstanceState.getInt(CURRENT_ITEM);
        } else {
            mStepModel = getArguments().getParcelable(STEP_MODEL);
            mCurrentItem = getArguments().getInt(CURRENT_ITEM);
            mId = getArguments().getInt(STEP_ID);
        }
        initListner();
        mView = inflater.inflate(R.layout.fragment_step_item, container, false);
        mPlayerView = mView.findViewById(R.id.video_view);
        noImageView = mView.findViewById(R.id.noVideoImage);
        TextView descriptionTv = mView.findViewById(R.id.stepDescription);
        descriptionTv.setText(mStepModel.getDescription());
        initializePlayer();
        if(mCurrentItem == mId){
            updatePlayerDisplay();
        }
        return mView;
    }

    private void updatePlayerDisplay(){
        if (!getContext().getResources().getBoolean(R.bool.isTablet)) {
            int orientation = getActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPlayer.removeListener(mPlayerListener);
                Intent intent = new Intent(getContext(), FullScreenVideoActivity.class);
                intent.putExtra(STEP_MODEL, mStepModel);
                getContext().startActivity(intent);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_MODEL, mStepModel);
        outState.putInt(CURRENT_ITEM, mCurrentItem);
        outState.putInt(STEP_ID, mId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void initListner() {
        if(mPlayerListener == null) {
            mPlayerListener = new SimpleExoPlayer.DefaultEventListener(){
                boolean isPlayerError;
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    super.onPlayerError(error);
                    Toast.makeText(getContext(), "An error has occured "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    noImageView.setVisibility(View.VISIBLE);
                    isPlayerError = true;
                }
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if(playbackState == Player.STATE_IDLE || playbackState == Player.STATE_READY) {
                        if(isPlayerError){
                            mPlayerView.setVisibility(View.INVISIBLE);
                            noImageView.setVisibility(View.VISIBLE);
                        } else {
                            mPlayerView.setVisibility(View.VISIBLE);
                            noImageView.setVisibility(View.INVISIBLE);
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
            mPlayer.removeListener(mPlayerListener);
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

        noImageView.setVisibility(View.INVISIBLE);
        Uri uri = Uri.parse(mStepModel.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource, true, false);
        mPlayer.addListener(mPlayerListener);
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
    public void onViewChange(int position) {
        mCurrentItem = position;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}