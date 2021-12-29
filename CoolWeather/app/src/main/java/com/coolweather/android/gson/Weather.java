package com.coolweather.android.gson;

import android.app.Notification;
import android.widget.LinearLayout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Create by lee on 2021/12/26 17:34
 */
public class Weather {
    @SerializedName("daily_forecast")
    public List<Forcast> forcastList;
    @SerializedName("liefstyle")
    public List<Lifestyle> lifestylesList;
    public String status;
    public Basic basic;
    public Update update;
    public Now now;

}
