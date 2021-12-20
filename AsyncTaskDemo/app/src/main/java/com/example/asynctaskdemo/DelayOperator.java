package com.example.asynctaskdemo;

public class DelayOperator {
    public void delay(){
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
