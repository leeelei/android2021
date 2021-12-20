package com.example.listviewtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;

public class AnimalActivity extends AppCompatActivity {
    private List<Animal> mData=null;
    private Context mContext;
    private AnimalAdapter mAdapter=null;
    private ListView list_animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal);
        mContext=AnimalActivity.this;
        list_animal=(ListView)findViewById(R.id.list_animal);
        mData=new LinkedList<Animal>();
        mData.add(new Animal("狗说","你是狗么？",R.mipmap.dog));
        mData.add(new Animal("牛说","你是牛么？",R.mipmap.cow));
        mData.add(new Animal("鸭说","你是鸭么？",R.mipmap.duck));
        mData.add(new Animal("鱼说","你是鱼么？",R.mipmap.fish));
        mData.add(new Animal("马说","你是马么？",R.mipmap.horce));

        mAdapter=new AnimalAdapter((LinkedList<Animal>)mData,mContext);
        list_animal.setAdapter(mAdapter);
    }
}