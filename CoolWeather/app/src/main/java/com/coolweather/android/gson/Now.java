package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Create by lee on 2021/12/26 16:51
 */
public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond_txt")
    public String info;
    public String cond_code;

    public String f1;
    public String wind_dir;
    public String wind_sc;
    public String wind_spd;
    public String hum;
    public String pcpn;
    public String pres;



}
