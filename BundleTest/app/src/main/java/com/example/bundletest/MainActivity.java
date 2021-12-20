package com.example.bundletest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private View.OnClickListener cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=(Button)findViewById(R.id.button1);
        cl=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,Target.class);
                Bundle mBundle=new Bundle();
                mBundle.putString("Data","data from TestBundle");
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        };
        button1.setOnClickListener(cl);
    }
}