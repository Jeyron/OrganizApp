package com.example.jeiro.organizapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import java.io.File;

public class VideoPreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        LinearLayout lLinLayout = new LinearLayout(this);

        lLinLayout.setOrientation(LinearLayout.VERTICAL);
        lLinLayout.setGravity(Gravity.CENTER);
        lLinLayout.setBackgroundColor(Color.BLACK);

        ActionBar.LayoutParams lLinLayoutParms = new ActionBar.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        lLinLayout.setLayoutParams(lLinLayoutParms);

        this.setContentView(lLinLayout);


        RelativeLayout lRelLayout = new RelativeLayout(this);
        lRelLayout.setGravity(Gravity.CENTER);
        lRelLayout.setBackgroundColor(Color.BLACK);
        android.widget.RelativeLayout.LayoutParams lRelLayoutParms =
                new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
        lRelLayout.setLayoutParams(lRelLayoutParms);
        lLinLayout.addView(lRelLayout);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final VideoView mVideoView;
        Intent intent = getIntent();
        Uri uri = Uri.parse(intent.getStringExtra("path"));
        MediaController mediaController = new MediaController(this);
        mVideoView = new VideoView(this);
        android.widget.RelativeLayout.LayoutParams lVidViewLayoutParams =
                new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);

        lVidViewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mVideoView.setLayoutParams(lVidViewLayoutParams);
        mVideoView.setVideoURI(uri);
        mediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mediaController);
        mVideoView.start();
        lRelLayout.addView(mVideoView);
    }
}
