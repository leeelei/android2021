package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    private DownloadBinder mBbinder=new DownloadBinder();
    class DownloadBinder extends Binder{
        public void startDownload(){
            Log.d("MyService", "startDownload executed");
        }
        public int getProgress(){
            Log.d("MyService", "getProgress executed");
            return 0;
        }
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBbinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d("MyService", "onCreate: executed");
        Intent intent=new Intent(this,MainActivity.class);
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

        Notification notification=new NotificationCompat.Builder(this,id).setContentTitle("This is content title").setContentText("This is content text").setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher)).setContentIntent(pi).setAutoCancel(true).build();
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService", "onStartCommand: executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService", "onDestroy: executed");
    }
}
