package com.example.notificationtest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity<onclick> extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendNotice=(Button)findViewById(R.id.send_notice);
        sendNotice.setOnClickListener((View.OnClickListener) this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.send_notice:
                //click function
                Intent intent=new Intent(this,NotificationActivity.class);
                PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);

                //make notification
                NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "channel_1"; //自定义设置通道ID属性
                String description = "123";//自定义设置通道描述属性
                int importance = NotificationManager.IMPORTANCE_HIGH;//通知栏管理重要提示消息声音设定
                NotificationChannel mChannel = new NotificationChannel(id, "123", importance);//建立通知栏通道类（需要有ID，重要属性）
                manager.createNotificationChannel(mChannel);////最后在notificationmanager中创建该通知渠道

                if(Build.VERSION.SDK_INT  >= android.os.Build.VERSION_CODES.O){

                }

                Notification  notification=new NotificationCompat.Builder(this,id).setContentTitle("This is content title").setContentText("This is content text").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).setContentIntent(pi).setAutoCancel(true).build();
                manager.notify(1,notification);

                break;
            default:
                break;
        }
    }
}