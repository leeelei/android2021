package com.coolweather.android.db;

import org.litepal.crud.LitePalSupport;

/**
 * Create by lee on 2021/12/23 16:54
 */
public class County extends LitePalSupport {
    private int id;
    private String countyName;
    private String weather_id;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weather_id;
    }

    public void setWeatherId(String weatherId) {
        this.weather_id = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
