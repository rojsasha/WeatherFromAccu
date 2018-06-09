package com.example.rojsa.weatherfromaccu.data.db;

import io.realm.RealmObject;

public class SaveForecastData extends RealmObject {
    public SaveForecastData(){}
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    private String temperature;
}
