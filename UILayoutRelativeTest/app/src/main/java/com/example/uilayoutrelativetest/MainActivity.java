package com.example.uilayoutrelativetest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Button relative1=(Button)findViewById(R.id.relative1);
        Button relative2=(Button)findViewById(R.id.relative2);
        Button frameLayoutDemo=(Button)findViewById(R.id.frameLayoutId);
        Button frameLayoutDemo2=(Button)findViewById(R.id.frameLayoutId2);
        Button titleBack=(Button)findViewById(R.id.title_back);
        Button titleEdit=(Button)findViewById(R.id.title_edit);
        Button progressBar=(Button)findViewById(R.id.progress_bar);
        Button progress_animation=(Button)findViewById(R.id.progress_animation);
        Button btnLayout=(Button)findViewById(R.id.btnLayout);
        Button dynamic_load=(Button)findViewById(R.id.dynamic_load);
        Button xmlLayoutActivity=(Button)findViewById(R.id.xmlLayoutActivity);
        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RelativeLayoutdemo.class);
                startActivity(intent);
            }
        });
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RelativeLayoutDemo2.class);
                startActivity(intent);
            }
        });
        frameLayoutDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, FrameLayoutDemo.class);
                startActivity(intent);
            }
        });

        frameLayoutDemo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, FrameLayoutDemo2.class);
                startActivity(intent);
            }
        });
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)getApplicationContext()).finish();
            }
        });
        titleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"You clicked Edit button",Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ProgressBar.class);
                startActivity(intent);
            }
        });

        progress_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ProgressBar2.class);
                startActivity(intent);
            }
        });
        btnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PuredJavaLayout.class);
                startActivity(intent);
            }
        });
        dynamic_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,JavaDynamicLoadLayout.class);
                startActivity(intent);
            }
        });
        xmlLayoutActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,XMLLayoutActivity.class);
                startActivity(intent);
            }
        });
    }

}