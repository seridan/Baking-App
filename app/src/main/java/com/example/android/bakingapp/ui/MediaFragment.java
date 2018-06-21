package com.example.android.bakingapp.ui;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.R;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MediaFragment extends Fragment implements View.OnClickListener {

    private List<Step> mSteps;
    private int stepId;
    private Step selectedStep;
    private String selectedStepDescription;
    private String selectedStepShortDescription;
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

    @BindView(R.id.step_short_description_tv)
    TextView mStepShortDescriptionTv;

    @BindView(R.id.step_instruction_tv)
    TextView mStepDescriptionTv;

    @BindView(R.id.playerView)
    PlayerView mPlayerView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.btn_next)
    Button mNextBtn;

    @BindView(R.id.btn_prev)
    Button mPrevBtn;

    private SimpleExoPlayer mExoplayer;


    public MediaFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        ButterKnife.bind(this, rootView);
        Timber.plant(new Timber.DebugTree());

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        final Bundle stepBundle = getArguments();

        if (stepBundle != null) {

            stepId = stepBundle.getInt(ARG_STEP_ID);
            mSteps = stepBundle.getParcelableArrayList(ARG_STEPS_LIST);
        }

        if (mSteps != null) {
            setStepDetail(stepId);
        }

        checkOrientationMode();

        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);



        return rootView;

    }

    private void setStepDetail(int id){

        selectedStep = mSteps.get(id);

        selectedStepDescription = selectedStep.getDescription();
        mStepDescriptionTv.setText(selectedStepDescription);

        selectedStepShortDescription = selectedStep.getShortDescription();
        mStepShortDescriptionTv.setText(selectedStepShortDescription);


        if (selectedStep.getVideoURL() != null){
            selectedStepVideoUrl = selectedStep.getVideoURL();
        }else {
            selectedStepVideoUrl = selectedStep.getThumbnailURL();
        }
        mToolbar.setTitle("Step: " + String.valueOf(id));
        initializePlayer(selectedStepVideoUrl);

    }

    private void initializePlayer(String mediaUrl){

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
        // Since the method .newSimpleInstance(context, trackSelector, loadControl) is deprecated, we use
        // newSimpleInstance(renderersFactory, trackSelector, loadControl);

        mExoplayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
        mPlayerView.setPlayer(mExoplayer);
        mExoplayer.setPlayWhenReady(playWhenReady);
        mExoplayer.seekTo(currentWindow, playbackPosition);

        if (mediaUrl.equals("") || mediaUrl.isEmpty()) {
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name));
            mPlayerView.hideController();

        }else {
            Uri mediaUri = Uri.parse(mediaUrl);
            MediaSource mediaSource = buildMediaSource(mediaUri);
            mExoplayer.prepare(mediaSource, true, false);

        }



    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        checkOrientationMode();

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

        if ((Util.SDK_INT <= 23 || mExoplayer == null)) {
            initializePlayer(selectedStepVideoUrl);

        }
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

    private void checkOrientationMode(){
        int currentOrientation = getResources().getConfiguration().orientation;


        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            Timber.d("Orientation mode: Landscape " + String.valueOf(currentOrientation));
                //Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }

            mStepShortDescriptionTv.setVisibility(View.GONE);
            mStepDescriptionTv.setVisibility(View.GONE);
            mNextBtn.setVisibility(View.GONE);
            mPrevBtn.setVisibility(View.GONE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(layoutParams);


            //getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        }
        else {
            Timber.d("Orientation mode: Portrait " + String.valueOf(currentOrientation));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
                //showSystemUI();
                Objects.requireNonNull(getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


            }
            mStepShortDescriptionTv.setVisibility(View.VISIBLE);
            mStepDescriptionTv.setVisibility(View.VISIBLE);
            mNextBtn.setVisibility(View.VISIBLE);
            mPrevBtn.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mPlayerView.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.height = 0;
            mPlayerView.setLayoutParams(layoutParams);

            //getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);


            //showSystemUI();

        }

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_prev:
                releasePlayer();
                if (stepId > 0){
                    stepId--;
                    setStepDetail(stepId);
                    } else {
                    Toast.makeText(getContext(), R.string.first_step_toast, Toast.LENGTH_SHORT).show();
                }
                    break;

            case R.id.btn_next:
                releasePlayer();
                if (stepId +1 < mSteps.size()){
                    stepId++;
                    setStepDetail(stepId);
                    } else {
                    Toast.makeText(getContext(), R.string.last_step_toast, Toast.LENGTH_SHORT).show();
                }
                    break;

        }

    }

}
