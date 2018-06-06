package com.example.rojsa.weatherfromaccu.data.db;

import io.realm.RealmObject;

public class SaveCityModel extends RealmObject {

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    private String idCity;
    private String CityName;
}
