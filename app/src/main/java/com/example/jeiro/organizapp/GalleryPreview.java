package com.example.jeiro.organizapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class GalleryPreview extends AppCompatActivity {

    ImageView GalleryPreviewImg;
    int posicion;
    ArrayList<String> pathList;
    ArrayList<String> typeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Intent intent = getIntent();
        posicion = intent.getIntExtra("posicion", 0);
        pathList = intent.getStringArrayListExtra("pathList");
        typeList = intent.getStringArrayListExtra("typeList");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        set_content(posicion);

    }

    private void set_content(final int poss)
    {
        if(typeList.get(+poss).equals(Function.PHOTO_TYPE))
            setContentView(R.layout.activity_gallery_preview_photo);
        else
        {
            setContentView(R.layout.activity_gallery_preview_video);
            ImageView img = (ImageView) findViewById(R.id.galeria_reproducir_video);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), VideoPreview.class);
                    intent.putExtra("path", pathList.get(+poss));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
        final GestureDetector gdt = new GestureDetector(new GestureListener());
        GalleryPreviewImg = (ImageView) findViewById(R.id.GalleryPreviewImg);
        /*
        GalleryPreviewImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                if(gdt.onTouchEvent(event))
                {
                    if(swap)
                        set_content((poss + 1) % pathList.size());
                    else
                        set_content((poss - 1 < 0)? pathList.size() - 1 : poss - 1);
                }
                return true;
            }
        });
        //*/
        if(typeList.get(+poss).equals(Function.PHOTO_TYPE))
            Glide.with(GalleryPreview.this)
                    .load(new File(pathList.get(+poss))) // Uri of the picture
                    .into(GalleryPreviewImg);
        else
            Glide.with(GalleryPreview.this)
                    .asBitmap()
                    .load(Uri.fromFile(new File(pathList.get(+poss)))) // Uri of the video
                    .into(GalleryPreviewImg);

    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private boolean swap;

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                swap = true;
                return true; // Right to left
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                swap = false;
                return true; // Left to right
            }

            /*
            if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            //*/
            return false;
        }
    }
}
