package com.example.networktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ResponseCache;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendReust=(Button)findViewById(R.id.send_reqeust);
        responseText=(TextView)findViewById(R.id.response_text);
        sendReust.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.send_reqeust){
            //sendRequestWithHttpRULConnection();

            //use okhttp
            sendRequestWithOkHttp();

        }
    }
    private void sendRequestWithHttpRULConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                BufferedReader reader=null;
                try{
                    //URL url=new URL("https://www.baidu.com");
                    URL url=new URL("http://192.168.0.103/get_data.json");
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in=connection.getInputStream();
                    reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(reader !=null){
                        try {
                            reader.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(connection !=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
    private void sendRequestWithOkHttp(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder().url("http://192.168.0.103/get_data.json").build();
                    Response response=client.newCall(request).execute();
                    String responseData=response.body().string();
                    //showResponse(responseData);
                    parseJSONWithJSON(responseData);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithJSON(String jsonData){
        try{
            JSONArray jsonArray=new JSONArray(jsonData);
            Log.d("MainActivity", String.valueOf(jsonArray.length()));
            String result=new String();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String id=jsonObject.getString("id");
                String name=jsonObject.getString("name");
                String version=jsonObject.getString("version");
                result +='\n'+"id is "+id+'\n'+"name is "+name+'\n'+"version is "+version+'\n';
            }
            showResponse(result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}