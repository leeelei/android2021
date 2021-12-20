package com.example.asynctaskdemo;

import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyAsyncTask extends AsyncTask<Integer,Integer,String> {
    private TextView txt;
    private ProgressBar pgbar;
    public MyAsyncTask(TextView txt, ProgressBar pgbar){
        super();
        this.txt=txt;
        this.pgbar=pgbar;
    }
    @Override
    protected String doInBackground(Integer... integers) {
        DelayOperator dop=new DelayOperator();
        int i=0;
        for(i=10;i<=100;i+=10){
            dop.delay();
            publishProgress(i);
        }
        return integers[0].intValue()+"";
    }

    @Override
    protected void onPreExecute() {
        txt.setText("开始执行异步线程");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //super.onProgressUpdate(values);
        int value=values[0];
        pgbar.setProgress(value);
    }
}
