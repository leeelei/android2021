package com.example.mobileinfoassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemActivity extends AppCompatActivity {
    private static final String TAG="MobiInfoAssisActivity";
    ListView itemlist=null;
    List<Map<String,Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        setTitle("系统信息");
        itemlist=(ListView)findViewById(R.id.itemlist);

    }

    private void refreshListItems(){
        list=buildListForSimpleAdapter();
        SimpleAdapter notes=new SimpleAdapter(this,list,R.layout.info_row,new String[]{"name","desc"},new int[]{R.id.name,R.id.desc});
        itemlist.setAdapter(notes);
        itemlist.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                Log.i(TAG,"Item clicked!["+position+"]");
                switch (position){
                    case 0:
                        intent.setClass(SystemActivity.this,SystemActivity.class);
                }
            }
        });
        itemlist.setSelection(0);

    }

    private List<Map<String,Object>> buildListForSimpleAdapter(){
        List<Map<String,Object>> list =new ArrayList<Map<String,Object>>(3);
        Map<String,Object> map=new HashMap<String,Object>();
        //map.put("id",PreferencesUtil.VER_INFO);
        map.put("desc","查看设备版本，运营商及其系统信息");
        map.put("img",R.drawable.system);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("name","硬件信息");
        map.put("desc","查看CPU,硬盘,内存等硬件信息");
        map.put("img",R.drawable.hardware);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("name","软件信息");
        map.put("desc","查看已安装的软件信息");
        map.put("img",R.drawable.software);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("name","运行时信息");
        map.put("desc","查看设备运行的信息");
        map.put("img",R.drawable.running);
        list.add(map);

        map=new HashMap<String,Object>();
        map.put("name","文件浏览器");
        map.put("desc","浏览查看文件");
        map.put("img",R.drawable.file_explorer);
        list.add(map);
        return  list;
    }
}