package com.example.bundletest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Target extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target);
        Bundle bundle=getIntent().getExtras();
        String data=bundle.getString("Data");
        setTitle(data);
    }
}