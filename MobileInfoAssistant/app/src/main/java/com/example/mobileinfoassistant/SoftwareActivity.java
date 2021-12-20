package com.example.mobileinfoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SoftwareActivity extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software);
        setTitle("软件信息");

    }

    @Override
    public void run() {

    }
}