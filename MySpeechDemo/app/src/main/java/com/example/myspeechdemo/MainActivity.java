package com.example.myspeechdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText main_edit_text;
    private Button main_btn_read;
    private TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTextToSpeech();
    }

    private void initTextToSpeech(){
        mTextToSpeech=new TextToSpeech(this, (TextToSpeech.OnInitListener) this);
        mTextToSpeech.setPitch(1.0f);
        mTextToSpeech.setSpeechRate(0.5f);
    }
    private void initView(){
        main_edit_text=(EditText)findViewById(R.id.main_edit_text);
        main_btn_read=(Button)findViewById(R.id.main_btn_read);
        main_btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.main_btn_read:
                        submit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void submit(){
        String text=main_edit_text.getText().toString().trim();
        if(TextUtils.isEmpty(text)){
            Toast.makeText(this,"请您输入要朗读的文字",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(mTextToSpeech !=null && !mTextToSpeech.isSpeaking()){
            mTextToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        mTextToSpeech.stop();
        mTextToSpeech.shutdown();
    }

    @Override
    protected void onDestroy() {
        if(mTextToSpeech !=null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
            mTextToSpeech=null;
        }
        super.onDestroy();
    }
}