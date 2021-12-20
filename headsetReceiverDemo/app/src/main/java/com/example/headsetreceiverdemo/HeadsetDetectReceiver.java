package com.example.headsetreceiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

public class HeadsetDetectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if(Intent.ACTION_HEADSET_PLUG.equals(action)){
            if(intent.hasExtra("state")){
                int state=intent.getIntExtra("state",0);
                if(state==1){
                    Toast toast=Toast.makeText(context,"插入耳机",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else if(state==0){
                    Toast toast=Toast.makeText(context,"拔出耳机",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            }
        }
    }
}
