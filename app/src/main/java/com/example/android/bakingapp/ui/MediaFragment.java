package com.example.android.bakingapp.ui;


import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;


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
    private String selectedStepThumbnailUrl;
    private Boolean isTwoPane;
    private static final String ARG_STEPS_LIST = "stepsList";
    private static final String ARG_STEP_ID = "stepId";
    private static final String ARG_TWO_PANE = "isTwoPane";
    private static final String PLAYER_POSITION = "playerPosition";
    private static final String PLAY_WHEN_READY = "playWhenReady";
    private boolean playWhenReady;
    private int currentWindow = 0;
    private long playbackPosition = 0 ;

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

    @BindView(R.id.media_image)
    ImageView mImageView;

    private SimpleExoPlayer mExoplayer;


    public MediaFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle stepBundle = getArguments();

        if (stepBundle != null) {

            stepId = stepBundle.getInt(ARG_STEP_ID);
            mSteps = stepBundle.getParcelableArrayList(ARG_STEPS_LIST);
            isTwoPane = stepBundle.getBoolean(ARG_TWO_PANE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media_player, container, false);

        ButterKnife.bind(this, rootView);
        Timber.plant(new Timber.DebugTree());

        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        if (mSteps != null) {
            setStepDetail(stepId);
        }

        mPrevBtn.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);

        return rootView;
    }

    public void setStepDetail(int id) {

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

        if (!selectedStep.getThumbnailURL().endsWith(".mp4")){
            selectedStepThumbnailUrl = selectedStep.getThumbnailURL();
            getImageFromUrl(selectedStepThumbnailUrl);
        }

            mToolbar.setTitle("Step: " + String.valueOf(id));
            initializePlayer(selectedStepVideoUrl);
        }

    public void getImageFromUrl (String url){

        if (url != null && !url.isEmpty() && !url.contains("")){
            Picasso.with(getContext())
                    .load(url)
                    .into(mImageView);
        } else {
            Picasso.with(getContext())
                    .load(R.drawable.recipe)
                    .into(mImageView);
        }
    }


    private void initializePlayer(String mediaUrl){

        if(mExoplayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());
            // Since the method .newSimpleInstance(context, trackSelector, loadControl) is deprecated, we use
            // newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mExoplayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);
            mExoplayer.setPlayWhenReady(playWhenReady);

            if (mediaUrl.equals("") || mediaUrl.isEmpty()) {
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name));
                mPlayerView.hideController();
            } else {
                Uri mediaUri = Uri.parse(mediaUrl);
                MediaSource mediaSource = buildMediaSource(mediaUri);
                mExoplayer.prepare(mediaSource, true, false);
            } // if is not null set to playbackposition stored in the bundle.
            if (playbackPosition != 0) {
                mExoplayer.seekTo(currentWindow, playbackPosition);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mExoplayer != null) {
            playbackPosition = mExoplayer.getCurrentPosition();
            outState.putLong(PLAYER_POSITION, playbackPosition);
            playWhenReady = mExoplayer.getPlayWhenReady();
            outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isTwoPane) {
            checkOrientationMode();
        }
    }


    private MediaSource buildMediaSource(Uri mediaUri) {

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-bakingApp")).
                createMediaSource(mediaUri);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isTwoPane) {
            mNextBtn.setVisibility(View.GONE);
            mPrevBtn.setVisibility(View.GONE);
        }

        if (!isTwoPane) {
            checkOrientationMode();
        }

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

        }
        else {
            Timber.d("Orientation mode: Portrait " + String.valueOf(currentOrientation));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_prev:
                releasePlayer();
                if (stepId > 0){
                    stepId--;
                    playbackPosition = 0; //set at the beginning of the video when clicked.
                    setStepDetail(stepId);
                    } else {
                    Toast.makeText(getContext(), R.string.first_step_toast, Toast.LENGTH_SHORT).show();
                }
                    break;

            case R.id.btn_next:
                releasePlayer();
                if (stepId +1 < mSteps.size()){
                    stepId++;
                    playbackPosition = 0; //set at the beginning of the video when clicked.
                    setStepDetail(stepId);
                    } else {
                    Toast.makeText(getContext(), R.string.last_step_toast, Toast.LENGTH_SHORT).show();
                }
                    break;
        }
    }
}
