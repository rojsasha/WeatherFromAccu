package com.example.rojsa.weatherfromaccu.data.db;

import io.realm.RealmObject;

public class SaveWeatherCurrent extends RealmObject {

private String timeCurrent;
private String temperature;
private String textWeather;

    public String getTimeCurrent() {
        return timeCurrent;
    }

    public void setTimeCurrent(String timeCurrent) {
        this.timeCurrent = timeCurrent;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTextWeather() {
        return textWeather;
    }

    public void setTextWeather(String textWeather) {
        this.textWeather = textWeather;
    }
}
