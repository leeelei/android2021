package com.example.uilayoutrelativetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class XMLLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_layout);
        //get object of LayoutInflater 获得Inflater对象,同时加载被添加的布局的xml,通过findViewById找到最外层的根节点
        final LayoutInflater inflater=LayoutInflater.from(this);
        //get object of outside container,也就是activity_dynamic_layout.xml文件的布局名字
        final RelativeLayout rly=(RelativeLayout)findViewById(R.id.RelativeLayout2);
        Button btnload=(Button)findViewById(R.id.btnLoad);
        btnload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //加载要添加的布局对象
                LinearLayout ly=(LinearLayout)inflater.inflate(R.layout.inflate,null,false).findViewById(R.id.ly_inflate);
                //为这个容器设置大小与位置信息:
                RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                //添加到外层容器中:
                rly.addView(ly,lp);
            }
        });

    }
}