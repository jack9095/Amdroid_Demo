package com.example.expandtextview.bean;

/**
 * @作者: njb
 * @时间: 2019/8/28 18:20
 * @描述:
 */
public class WeatherEvent {
    private String id;
    private String cityName;
    private String temperature;

    public WeatherEvent(String id, String cityName,String temperature) {
        this.id = id;
        this.cityName = cityName;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
