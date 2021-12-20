package com.example.bindservicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestService2 extends Service {

    private final String TAG="TestService2";
    private int count;
    private boolean quit;
    private MyBinder binder=new MyBinder();
    public class MyBinder extends Binder {
        public int getCount(){
            return count;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBinder方法被调用！");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate方法被调用");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!quit){
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    count++;
                }
            };
        }).start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //return super.onUnbind(intent);
        Log.i(TAG,"onBind方法被调用");
        return  true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.quit=true;
        Log.i(TAG,"onDestroy方法被调用");
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG,"onRebind方法被调用");
        super.onRebind(intent);
    }
}
