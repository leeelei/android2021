package com.example.servicebestpractice;

import android.os.AsyncTask;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SUCCESS=0;
    public static final int TYPE_FAILED=1;
    public static final int TYPE_PAUSED=2;
    public static final int TYPE_CANCEL=3;
    private DownloadListener listener;
    private boolean isCancled=false;
    private boolean isPaused=false;
    private int lastProgress;
    public DownloadTask(DownloadListener listener){
        this.listener=listener;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        InputStream is=null;
        RandomAccessFile saveFile=null;
        File file=null;
        try{
            long downloadedLength=0;
            String downloadUrl=strings[0];
            String fileName=downloadUrl.substring(downloadUrl.lastIndexOf("/"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
