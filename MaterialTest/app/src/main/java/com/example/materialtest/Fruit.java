package com.example.materialtest;

/**
 * Create by lee on 2021/12/27 19:34
 */
public class Fruit {
    private String name;
    private int imageId;
    public Fruit(String name,int imageId){
        this.imageId=imageId;
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
}
