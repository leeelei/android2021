package com.example.phoneelectricitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.security.identity.CipherSuiteNotSupportedException;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private BatteryReceiver receiver=null;
    private TextView tv;
    private Button btn=null;
    private TextToSpeech mTextToSpeech;
    private TextView tvGetVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTextToSpeech();
        tvGetVersion=(TextView)findViewById(R.id.tvgetVersion);
    }
    private void initTextToSpeech(){
        mTextToSpeech=new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
        mTextToSpeech.setPitch(1.0f);
        mTextToSpeech.setSpeechRate(0.5f);
    }
    private void initView(){
        receiver=new BatteryReceiver();
        tv=(TextView)findViewById(R.id.tv);
        btn=(Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter filter=new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                registerReceiver(receiver,filter);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(mTextToSpeech !=null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech=null;
        }
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
    }

    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS){
            int result=mTextToSpeech.setLanguage(Locale.CHINA);
            if(result==TextToSpeech.LANG_MISSING_DATA || result== TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"数据丢失或不支持",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class BatteryReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int current = intent.getExtras().getInt("level");
            int total=intent.getExtras().getInt("scale");
            int percent=current*100/total;
            tv.setText("当前电量："+percent+"%");
            submit();
        }
    }

    private void submit(){
        String text=tv.getText().toString().trim();
        if(TextUtils.isEmpty(text)){
            Toast.makeText(this,"请您输入要朗读的文字",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(mTextToSpeech !=null && !mTextToSpeech.isSpeaking()){
            mTextToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

        public void getVersion(View v){
            String version= Build.VERSION.SDK_INT+"";
            tvGetVersion.setText("当前操作系统的版本号为："+version);
        }
    /***
     * 获取Android Linux内核版本信息
     */
    public void getLinuxKernalInfo(View v) {
        tvGetVersion.setText("----Linux Kernal is : "+"1000");
        Process process = null;
        String mLinuxKernal = null;
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
                // result += "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (result != "") {
            String Keyword = "version ";
            int index = result.indexOf(Keyword);
            Log.v("MainActivity", "----"+result);
            line = result.substring(index + Keyword.length());
            index = line.indexOf(" ");
            // tv01.setText(line.substring(0,index));
            mLinuxKernal = line.substring(0, index);
            tvGetVersion.setText("----Linux Kernal is : "+mLinuxKernal);
            Log.d("MainActivity", "----Linux Kernal is : " + mLinuxKernal);
        }
    }

    public void getLinuxKernalInfoEx(View v) {
        String result = "";
        String line;
        String[] cmd = new String[] { "/system/bin/cat", "/proc/version" };
        String workdirectory = "/system/bin/";
        try {
            ProcessBuilder bulider = new ProcessBuilder(cmd);
            bulider.directory(new File(workdirectory));
            bulider.redirectErrorStream(true);
            Process process = bulider.start();
            InputStream in = process.getInputStream();
            InputStreamReader isrout = new InputStreamReader(in);
            BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

            while ((line = brout.readLine()) != null) {
                result += line;
                // result += "\n";
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
       // Log.i(TAG,"----Linux Kernal is :"+result);
        tvGetVersion.setText("----Linux Kernal is : "+result);
      //  return result;
    }


}