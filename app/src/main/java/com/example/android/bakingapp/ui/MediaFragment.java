package com.example.android.bakingapp.ui;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaFragment extends Fragment {

    private List<Step> mSteps;
    private int stepId;
    private Step selectedStep;
    private String selectedStepDescription;
    private String selectedStepVideoUrl;
    private Recipe selectedRecipe;
    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEP = "step";
    private static final String ARG_VIDEO = "video";
    private static final String ARG_STEPS_LIST = "stepsList";
    private static final String ARG_STEP_ID = "stepId";
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    @BindView(R.id.step_instruction_tv)
    TextView mStepDescriptionTv;

    @BindView(R.id.playerView)
    PlayerView mPlayerView;

    private SimpleExoPlayer mExoplayer;


    public MediaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        ButterKnife.bind(this, rootView);


        final Bundle stepBundle = getArguments();

        if (stepBundle != null) {

            stepId = stepBundle.getInt(ARG_STEP_ID);
            mSteps = stepBundle.getParcelableArrayList(ARG_STEPS_LIST);
            //selectedStepDescription = recipeBundle.getString(ARG_STEP);
            //selectedStepVideoUrl = recipeBundle.getString(ARG_VIDEO);
            //mStepDescriptionTv.setText(selectedStepDescription);
        }

        if (mSteps != null) {
            setStepDetail(stepId, mSteps);
        }

        initializePlayer(selectedStepVideoUrl);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.nutella));
        return rootView;

    }

    private void setStepDetail(int id, List<Step> stepList){

        selectedStep = stepList.get(id);
        selectedStepDescription = selectedStep.getDescription();
        if (selectedStep.getVideoURL() != null){
            selectedStepVideoUrl = selectedStep.getVideoURL();
        }else {
            selectedStepVideoUrl = selectedStep.getThumbnailURL();
        }

    }

    private void initializePlayer(String mediaUrl){

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
        // Since the method .newSimpleInstance(context, trackSelector, loadControl) is deprecated, we use
        // .newSimpleInstance(renderersFactory, trackSelector, loadControl);
        mExoplayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        mPlayerView.setPlayer(mExoplayer);
        mExoplayer.setPlayWhenReady(playWhenReady);
        mExoplayer.seekTo(currentWindow, playbackPosition);

        Uri mediaUri = Uri.parse(mediaUrl);
        MediaSource mediaSource = buildMediaSource(mediaUri);
        mExoplayer.prepare(mediaSource, true, false);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private MediaSource buildMediaSource(Uri mediaUri) {

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-bakingApp")).
                createMediaSource(mediaUri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(selectedStepVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mExoplayer == null)) {
            initializePlayer(selectedStepVideoUrl);
        }
    }

    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            playbackPosition = mExoplayer.getCurrentPosition();
            currentWindow = mExoplayer.getCurrentWindowIndex();
            playWhenReady = mExoplayer.getPlayWhenReady();
            mExoplayer.release();
            mExoplayer = null;
        }
    }


}
