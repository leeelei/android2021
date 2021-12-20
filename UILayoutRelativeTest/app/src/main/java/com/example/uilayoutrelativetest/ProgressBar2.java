package com.example.uilayoutrelativetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.animation.Animation;
import android.widget.ImageView;

public class ProgressBar2 extends AppCompatActivity {
    private ImageView img_pgbar;
    private AnimationDrawable ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar2);
        img_pgbar=(ImageView)findViewById(R.id.img_pgbar);
        ad=(AnimationDrawable)img_pgbar.getDrawable();
        img_pgbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                ad.start();
            }
        },100);
    }
}