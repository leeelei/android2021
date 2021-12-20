package com.example.xutilstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private EditText et_path;
    private ProgressBar pb;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
          "android.permission.READ_EXTERNAL_STORAGE",
          "android.permission.WRITE_EXTERNAL_STORAGE" };
    private String fileName;
    private String appName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        x.view().inject(this);
        et_path=findViewById(R.id.et_path);
        pb=findViewById(R.id.progressBar);

    }
    public static void verifyStoragePermissions(Activity activity){
        try{
            int permission= ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void click(View view){
        String path=et_path.getText().toString().trim();
        fileName=getFileName(path);
        RequestParams params=new RequestParams(path);
        params.setSaveFilePath(fileName);
        params.setAutoRename(true);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {
                System.out.println("下载完成");
                Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("测试2");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("测试3");
            }

            @Override
            public void onFinished() {
                System.out.println("测试4");
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                pb.setMax((int) total);
                pb.setProgress((int)current);
                System.out.println("测试1");
            }
        });
    }
    public String getFileName(String path){
        int start=path.lastIndexOf("/")+1;
        appName=path.substring(start);
        String fileName="sdcard/file/"+appName;
        return fileName;
    }
}