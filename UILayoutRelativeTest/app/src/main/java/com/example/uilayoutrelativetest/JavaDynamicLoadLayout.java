package com.example.uilayoutrelativetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

public class JavaDynamicLoadLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Don't need to use setContentView method。
        //setContentView(R.layout.java_dynamic_load_layout);
        Button btnOne=new Button(this);
        btnOne.setText("我是动态添加的按钮");
        RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.CENTER_IN_PARENT);
        LayoutInflater inflater=LayoutInflater.from(this);
        RelativeLayout rly=(RelativeLayout)inflater.inflate(R.layout.java_dynamic_load_layout,null);
        rly.addView(btnOne,lp2);
        setContentView(rly);

    }
}