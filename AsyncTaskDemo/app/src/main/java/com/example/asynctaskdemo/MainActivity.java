package com.example.asynctaskdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private TextView txttitle;
    private TextView texten;
    private ProgressBar pgbar;
    private Button btnupdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txttitle=(TextView) findViewById(R.id.txttitle);
        pgbar=(ProgressBar)findViewById(R.id.pgbar);
        btnupdate=(Button)findViewById(R.id.btnupdate);
        Button btnEn=(Button)findViewById(R.id.btnen);
        texten=(TextView)findViewById(R.id.EnText);
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAsyncTask myTask=new MyAsyncTask(txttitle,pgbar);
                myTask.execute(1000);
            }
        });
        btnEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //外部存储设备的当前状态
                String result="------ExternalStorage---"+Environment.getExternalStorageState()+'\n';
                texten.setText(result);
                // 某种类型文件的路径，比如下面是图片的 打印结果/storage/emulated/0/Pictures
                String dir="------RootDirectory---"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+'\n';
                //Log.e("------RootDirectory", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
                texten.append(dir);
                //获取 Android 数据目录 打印结果为 /data
                //Log.e("------getDataDirectory", Environment.getDataDirectory().toString());
                String datadir="------getDataDirectory---"+Environment.getDataDirectory().toString()+'\n';
                texten.append(datadir);
                // Android 下载/缓存内容目录 打印结果为/data/cache
                //Log.e("---DownloadCache", Environment.getDownloadCacheDirectory().toString());
                String downloadStr="---DownloadCache---"+Environment.getDownloadCacheDirectory().toString()+'\n';
                texten.append(downloadStr);
                //外部存储目录即 SDCard 打印结果为 /storage/emulated/0
                //Log.e("------ExternalStorage", Environment.getExternalStorageDirectory().toString());
                String sdcarStr="------ExternalStorage---"+Environment.getExternalStorageDirectory().toString()+'\n';
                texten.append(sdcarStr);
                // Android 的根目录 打印结果为 /system
                //Log.e("------RootDirectory", Environment.getRootDirectory().toString());
                String rootdir="------RootDirectory---"+Environment.getRootDirectory().toString()+'\n';
                texten.append(rootdir);
            }
        });
    }
}