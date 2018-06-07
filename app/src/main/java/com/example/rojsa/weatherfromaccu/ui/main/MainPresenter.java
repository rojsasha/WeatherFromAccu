package com.example.rojsa.weatherfromaccu.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.PermissionUtils;
import com.example.rojsa.weatherfromaccu.data.StringResources;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;
import com.example.rojsa.weatherfromaccu.models.geo_pos_model.GeoPosModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private WeatherInterface mService;
    private StringResources mGetString;
    private LatLng currentLocation;
    private LocationManager mLocationManager;
    private Location location;
    private boolean flag = true;


    public MainPresenter(WeatherInterface service, StringResources getString, LocationManager manager) {
        mService = service;
        mGetString = getString;
        mLocationManager = manager;
    }

    @Override
    public void getWeatherCurrent(String keyCity) {
        if (keyCity != null) {

        }

        mService.getCurrentWeather(keyCity, mGetString.getString(R.string.api_key1))
                .enqueue(new Callback<List<CurrentModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<CurrentModel>> call, @NonNull Response<List<CurrentModel>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            mView.onSuccessCurrentWeather(response.body().get(0));
                        } else {
                            mView.onError(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<CurrentModel>> call, @NonNull Throwable t) {
                        mView.onError(t.getLocalizedMessage());
                    }
                });
    }


    @Override
    public void getWeatherForecast(String keyCity) {
        if (keyCity != null) {
            mService.getCityForecastFiveDays(keyCity, mGetString.getString(R.string.api_key2), "ru-RU",
                    true, true)
                    .enqueue(new Callback<ForecastModel>() {
                        @Override
                        public void onResponse(@NonNull Call<ForecastModel> call, @NonNull Response<ForecastModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                mView.onSuccessForecastWeather(response.body());
                            } else {
                                mView.onError(response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ForecastModel> call, @NonNull Throwable t) {
                            mView.onError(t.getLocalizedMessage());
                        }
                    });
        } else {
            mView.onError("нету данных");
        }

    }

    @Override
    public void getLocationCurrentWeather(final String location) {
        Log.d("locat", "getLocationCurrentWeather: " + location );
        if (location != null) flag = false;
        mService.getGeoKeyCity(mGetString.getString(R.string.api_key),
                location
                , "ru-RU")
                .enqueue(new Callback<GeoPosModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GeoPosModel> call, @NonNull Response<GeoPosModel> response) {
                        if (response.isSuccessful() && response.body() != null){
                            getWeatherCurrent(response.body().getKey());
                            getWeatherForecast(response.body().getKey());
                            Log.d("locat", "getLocat: in onresponse" + response.body().getKey());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GeoPosModel> call, @NonNull Throwable t) {

                    }
                });



    }

    @Override
    public void bind(MainContract.View view) {
        mView = view;
        Log.d("startpresenter", "bind: ");
        getLocation(location);
    }


    @Override
    public void unbind() {
        mView = null;
    }

    public void getLocation(Location lastKnownLocation) {
        if (flag){
            if (PermissionUtils.checkLocationPermission((Activity) mView)){
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000 * 10, 10, locationListener);
                Log.d("location", "getLocation: permission ");
                getFormatLocation(lastKnownLocation);
            }
        }



    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            getLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @SuppressLint("MissingPermission")
        @Override
        public void onProviderEnabled(String s) {
            getLocation(mLocationManager.getLastKnownLocation(s));
        }

        @Override
        public void onProviderDisabled(String s) {

        }


    };


    @SuppressLint("DefaultLocale")
    private void getFormatLocation(Location location) {

        if (location == null) {
            return;
        }
        getLocationCurrentWeather(String.format("%1$.4f",
                location.getLatitude()).replace(",", ".")
                + ","
                + String.format("%1$.4f",
                location.getLongitude()).replace(",", "."));
    }
}
