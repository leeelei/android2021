package com.example.uilayoutrelativetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class PuredJavaLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pured_java_layout);
        RelativeLayout rly=new RelativeLayout(this);
        Button btnOne=new Button(this);
        btnOne.setText("按钮1");
        Button btnTwo=new Button(this);
        btnTwo.setText("按钮2");

        btnOne.setId(Integer.parseInt("123"));
        RelativeLayout.LayoutParams rlp1=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp1.addRule(RelativeLayout.CENTER_IN_PARENT);

        RelativeLayout.LayoutParams rlp2=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp2.addRule(RelativeLayout.BELOW,123);
        rly.addView(btnOne,rlp1);
        rly.addView(btnTwo,rlp2);
        setContentView(rly);

    }
}