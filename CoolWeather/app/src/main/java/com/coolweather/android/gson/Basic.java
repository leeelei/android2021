package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by lee on 2021/12/26 16:45
 */
public class Basic {
    @SerializedName("location")
    public String cityName;
    @SerializedName("cid")
    public String weatherId;
    public String lat;
    public String lon;

}
