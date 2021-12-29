package com.example.recycleranotherdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.ConditionVariable;

import java.util.ArrayList;
import java.util.List;

//1.获取RecyclerView对象 。
//2.初始化数据 。
//3.适配器实例化 。
//4.设置LayoutManager
//5. 设置Adapter 。
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecycleAdapterDome adapterDome;
    private Context context;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        recyclerView=(RecyclerView)findViewById(R.id.recyler_view);
        list=new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("这是第 "+i+" 个测试");
        }
        //实例化适配器
        adapterDome=new RecycleAdapterDome(context,list);
        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterDome);
    }
}