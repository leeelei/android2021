package com.example.mobileinfoassistant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CMDExecute {
    public synchronized String run(String[] cmd,String workdirectory) throws IOException {
        String result="";
        try{
            ProcessBuilder builder=new ProcessBuilder(cmd);
            if(workdirectory !=null)
                builder.directory(new File(workdirectory));
            builder.redirectErrorStream(true);
            Process process=builder.start();
            InputStream in=process.getInputStream();
            byte[] re=new byte[1024];
            while(in.read(re) !=-1){
                result=result+new String(re);
            }
            in.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  result;
    }
}
