package com.jekton.mobilelearn.course;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jekton.mobilelearn.R;

/**
 * @author Jekton
 */
public class VideoActivity extends AppCompatActivity {

    private Uri mVideoUri;
    private VideoView mVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initUrl();
        initVideo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        playVideo();
    }

    private void initUrl() {
        Intent intent = getIntent();
        mVideoUri = intent.getData();
        if (mVideoUri == null) {
            throw new IllegalArgumentException("Intent that used to start this activity"
                                                       + "must contain a uri data");
        }
    }

    private void initVideo() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFitsSystemWindows(true);
        mVideoView.setKeepScreenOn(true);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
    }

    private void playVideo() {
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.start();
    }

    public static Intent makeIntent(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.setData(Uri.parse(url));
        return intent;
    }


}
