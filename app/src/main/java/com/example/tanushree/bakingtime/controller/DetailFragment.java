package com.example.tanushree.bakingtime.controller;

// Date: 24.5.19


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment
{
    //private static final String TAG = DetailFragment.class.getSimpleName();
    private static final String KEY_STEP = "step";
    private static final String KEY_TWO_PANE = "two_pane";
    private static final String KEY_PLAYER_POSITION = "player_position";
    private static final String KEY_PLAY_STATE = "play_state";

    private Step mStep;
    private PlayerView mStepVideoPv;
    private SimpleExoPlayer mExoPlayer;
    private TextView mStepDescriptionTv;
    private ImageView mStepImageIv;
    private FrameLayout mFrameLayout;
    private LinearLayout mLinearLayout;
    private boolean mTwoPane;
    private long mPlayerPosition;
    private boolean mStartAutoPlay;

    //Mandatory constructor for instantiating the fragment.
    public DetailFragment()
    {
    }

    public void setStep(Step step, boolean twoPane)
    {
        mStep = step;
        mTwoPane = twoPane;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            mStep = savedInstanceState.getParcelable(KEY_STEP);
            mTwoPane = savedInstanceState.getBoolean(KEY_TWO_PANE);
            if(savedInstanceState.containsKey(KEY_PLAYER_POSITION)) {
                mPlayerPosition = savedInstanceState.getLong(KEY_PLAYER_POSITION);
                //Log.d(TAG, "Player position retrieved.");
            }
            if(savedInstanceState.containsKey(KEY_PLAY_STATE))
            {
                mStartAutoPlay = savedInstanceState.getBoolean(KEY_PLAY_STATE);
                //Log.d(TAG, "Play state retrieved.");
            }
        }

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mStepVideoPv = view.findViewById(R.id.pvStepVideo);

        mStepImageIv = view.findViewById(R.id.ivStepImage);

        mStepDescriptionTv = view.findViewById(R.id.tvStepDescription);

        if(mStep.getDescription().isEmpty())
            mStepDescriptionTv.setText(R.string.data_not_available);
        else
            mStepDescriptionTv.setText(mStep.getDescription());

        mFrameLayout = view.findViewById(R.id.frameLayout);
        mLinearLayout = view.findViewById(R.id.linearLayout);

        Configuration configuration = getActivity().getResources().getConfiguration();

        // If not a tablet device && In Landscape mode, go full screen.
        if (!mTwoPane && (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE))
        {
            //Log.d(TAG, "Phone in landscape orientation.");

            mFrameLayout.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT)
            );

            mStepVideoPv.setLayoutParams(
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT)
            );

            mStepImageIv.setLayoutParams(
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT)
            );

            fullScreen();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23)
        {
            setVideoOrImage();
            //Log.d(TAG, "onStart() called.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if((Util.SDK_INT <=23) || (mExoPlayer == null))
            setVideoOrImage();
    }

    private void setVideoOrImage()
    {
        // If video url is present.
        if(!mStep.getVideoUrl().equals(""))
        {
            mStepVideoPv.setVisibility(View.VISIBLE);
            mStepImageIv.setVisibility(View.INVISIBLE);
            initializePlayer(Uri.parse(mStep.getVideoUrl()));
        }
        // If video url is empty.
        else
        {
            String imageUrl = mStep.getImageUrl();
            // If image url is present.
            if(!imageUrl.equals(""))
            {
                // If the thumbnail image url contains a .mp4 (video) url instead.
                if(imageUrl.contains("."))
                {
                    String fileExtension = imageUrl.substring(imageUrl.lastIndexOf("."));
                    if(fileExtension.equals(".mp4"))
                    {
                        mStepVideoPv.setVisibility(View.VISIBLE);
                        mStepImageIv.setVisibility(View.INVISIBLE);
                        initializePlayer(Uri.parse(imageUrl));
                    }
                }
                else
                {
                    mStepVideoPv.setVisibility(View.INVISIBLE);
                    mStepImageIv.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(imageUrl)
                            .error(R.drawable.baking)
                            .into(mStepImageIv);
                }

            }
            // If thumbnail image url is empty.
            else
            {
                mStepVideoPv.setVisibility(View.INVISIBLE);
                mStepImageIv.setVisibility(View.VISIBLE);
                mStepImageIv.setImageResource(R.drawable.baking);
            }
        }
    }

    private void initializePlayer(Uri mediaUri)
    {
        if(mExoPlayer == null)
        {
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mStepVideoPv.setPlayer(mExoPlayer);

            //Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingTime");
            MediaSource mediaSource = new ExtractorMediaSource
                    .Factory(new DefaultDataSourceFactory(getActivity(), userAgent))
                    .createMediaSource(mediaUri);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mPlayerPosition);
            mExoPlayer.setPlayWhenReady(mStartAutoPlay);
        }
    }

    private void releasePlayer()
    {
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23)
            releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23) {
            releasePlayer();
            //Log.d(TAG, "OnStop() called.");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState)
    {
        //Log.d(TAG, "OnSaveInstanceState() called.");
        currentState.putParcelable(KEY_STEP, mStep);
        currentState.putBoolean(KEY_TWO_PANE, mTwoPane);
        if(mExoPlayer!=null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            currentState.putLong(KEY_PLAYER_POSITION, mPlayerPosition);
            //Log.d(TAG, "Player position saved.");
            mStartAutoPlay = mExoPlayer.getPlayWhenReady();
            currentState.putBoolean(KEY_PLAY_STATE, mStartAutoPlay);
            //Log.d(TAG, "Play state saved.");
        }
    }

    private void fullScreen() {
        hideSystemUI();
        mStepVideoPv.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        // Remove padding from layout
        mLinearLayout.setPadding(0, 0, 0, 0);
    }

    private void hideSystemUI() {

        // Hide the app bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Hide system nav and go full immersive
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}