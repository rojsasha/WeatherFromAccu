package com.example.rojsa.weatherfromaccu;

import android.app.Application;
import android.content.Context;

import com.example.rojsa.weatherfromaccu.data.internet.NetWorkBuilder;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;

/**
 * Created by rojsa on 07.04.2018.
 */

public class WeatherApplication extends Application {
    private WeatherInterface service;
    @Override
    public void onCreate() {
        super.onCreate();
        service = NetWorkBuilder.initRetrofit();
    }
    public static WeatherApplication get(Context context){
        return (WeatherApplication) context.getApplicationContext();
    }


    public WeatherInterface getService(){
        return service;
    }


}
