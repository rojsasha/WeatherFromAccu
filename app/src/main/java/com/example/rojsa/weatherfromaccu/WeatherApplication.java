package com.example.rojsa.weatherfromaccu;

import android.app.Application;
import android.content.Context;

import com.example.rojsa.weatherfromaccu.data.internet.NetWorkBuilder;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;

import io.realm.Realm;

/**
 * Created by rojsa on 07.04.2018.
 */

public class WeatherApplication extends Application {
    private WeatherInterface service;
    private Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        service = NetWorkBuilder.initRetrofit();
        Realm.init(this);
        realm = Realm.getDefaultInstance();


    }

    public static WeatherApplication get(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }


    public WeatherInterface getService() {
        return service;
    }

    public Realm getRealm() {
        return realm;
    }


}
