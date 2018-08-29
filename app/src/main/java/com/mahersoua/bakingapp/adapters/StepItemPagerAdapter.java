package com.mahersoua.bakingapp.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepItemPagerAdapter extends PagerAdapter {

    private static final String TAG = "StepItemPagerAdapter";
    private long currentPositionPlayer;
    private int currentWindowIndex;
    private boolean playWhenReady;
    private Dialog mFullScreenDialog;
    private boolean isFullScreen;
    private Context mContext;
    private List<StepModel> mList;
    private boolean isUpdateDone;
    private Map<Integer, SimpleExoPlayer> mPlayerConfig = new HashMap<>();

    public StepItemPagerAdapter(Context context, List<StepModel> list) {
        mContext = context;
        mList = list;
    }

    private SimpleExoPlayer.DefaultEventListener getListener(View view) {

        ImageView imageView = view.findViewById(R.id.noVideoImage);
        PlayerView playerView = view.findViewById(R.id.video_view);
        return new SimpleExoPlayer.DefaultEventListener(){

            boolean isPlayerError;
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                super.onPlayerError(error);
                imageView.setVisibility(View.VISIBLE);
                isPlayerError = true;
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                super.onPlayerStateChanged(playWhenReady, playbackState);
                if(playbackState == Player.STATE_IDLE || playbackState == Player.STATE_READY) {
                    if(isPlayerError){
                        playerView.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    } else {
                        playerView.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(R.layout.adapater_page_step_item, container, false);
        TextView descriptionTv = view.findViewById(R.id.stepDescription);
        descriptionTv.setText(mList.get(position).getDescription());
        initFullscreenDialog(view);
        initializePlayer(view, position);
        if(!isUpdateDone){
            isUpdateDone = true;
            updatePlayerDisplay(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View) view);
        releasePlayer(position);
    }

    private void initFullscreenDialog(View view) {
        mFullScreenDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (isFullScreen)
                    closeFullscreenDialog(view);
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog(View view) {
        PlayerView playerView = view.findViewById(R.id.video_view);
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.setContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        isFullScreen = true;
        mFullScreenDialog.show();
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
    }

    private void closeFullscreenDialog(View view) {
        PlayerView playerView = view.findViewById(R.id.video_view);
        if(playerView == null) return;
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        ((FrameLayout) view.findViewById(R.id.playerContainer)).addView(playerView);
        isFullScreen = false;
        mFullScreenDialog.dismiss();
    }

    private void updatePlayerDisplay(View view){
        if (!mContext.getResources().getBoolean(R.bool.isTablet)) {
            int orientation = mContext.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                closeFullscreenDialog(view);
            } else {
                openFullscreenDialog(view);
            }
        }
    }

    private void releasePlayer(int position) {
        SimpleExoPlayer  player = mPlayerConfig.get(position);
        if (player != null) {
            player.release();
            currentPositionPlayer = player.getCurrentPosition();
            currentWindowIndex = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
        }
    }

    private void initializePlayer(View view, int position) {
        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(), new DefaultLoadControl());
        PlayerView playerView = view.findViewById(R.id.video_view);
        ImageView noImageView = view.findViewById(R.id.noVideoImage);
        playerView.setPlayer(simpleExoPlayer);
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

        Uri uri = Uri.parse(mList.get(position).getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);
        simpleExoPlayer.prepare(mediaSource, true, false);

        noImageView.setVisibility(View.INVISIBLE);
        playerView.setVisibility(View.VISIBLE);
        SimpleExoPlayer.DefaultEventListener listener = getListener(view);
        simpleExoPlayer.addListener(listener);
        mPlayerConfig.put(position, simpleExoPlayer);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "BankingApp"), bandwidthMeter);
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        return videoSource;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
