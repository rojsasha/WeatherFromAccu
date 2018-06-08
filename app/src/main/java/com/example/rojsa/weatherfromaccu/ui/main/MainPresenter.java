package com.example.rojsa.weatherfromaccu.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.PermissionUtils;
import com.example.rojsa.weatherfromaccu.data.StringResources;
import com.example.rojsa.weatherfromaccu.data.db.SaveMainCity;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;
import com.example.rojsa.weatherfromaccu.models.geo_pos_model.GeoPosModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private WeatherInterface mService;
    private StringResources mGetString;
    private LocationManager mLocationManager;
    private Location location;
    private boolean flag = true;
    private Realm mRealm;


    public MainPresenter(WeatherInterface service,Realm realm, StringResources getString, LocationManager manager) {
        mService = service;
        mGetString = getString;
        mLocationManager = manager;
        mRealm = realm;
    }


    private void saveCity(String idCity, String nameCity, String location) {

        mRealm.beginTransaction();
        if (mRealm.where(SaveMainCity.class).findFirst() == null) {
            SaveMainCity mainCity = mRealm.createObject(SaveMainCity.class);
            mainCity.setIdCity(idCity);
            mainCity.setNameCity(nameCity);
            mainCity.setLocation(location);

        } else {
            SaveMainCity mainCity = mRealm.where(SaveMainCity.class).findFirst();
            mainCity.setIdCity(idCity);
            mainCity.setNameCity(nameCity);
            mainCity.setLocation(location);
        }
        mRealm.commitTransaction();
    }

    private void getSavedCoordinateComparison(String location) {
        SaveMainCity mainCity = mRealm.where(SaveMainCity.class).findFirst();
        if (mainCity != null) {
            flag = false;
            mView.onSuccessLocationCurrentWeather(mainCity.getNameCity());
            getWeatherCurrent(mainCity.getIdCity());
            getWeatherForecast(mainCity.getIdCity());
            Log.d("getsavedcity", "getSavedCoordinateComparison: ");
        } else {
            getLocationCurrentWeather(location);
        }
    }

    @Override
    public void getWeatherCurrent(String keyCity) {
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
        Log.d("locat", "getLocationCurrentWeather: " + location);
        mService.getGeoKeyCity(mGetString.getString(R.string.api_key2),
                location
                , "ru-RU")
                .enqueue(new Callback<GeoPosModel>() {
                    @Override
                    public void onResponse(@NonNull Call<GeoPosModel> call, @NonNull Response<GeoPosModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            saveCity(response.body().getKey(),
                                    response.body().getLocalizedName(),
                                    location);
                            getWeatherCurrent(response.body().getKey());
                            getWeatherForecast(response.body().getKey());
                            mView.onSuccessLocationCurrentWeather(response.body().getLocalizedName());
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
        if (flag) {
            if (PermissionUtils.checkLocationPermission((Activity) mView)) {
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

        String formattedLocation = String.format("%1$.4f",
                location.getLatitude()).replace(",", ".")
                + ","
                + String.format("%1$.4f",
                location.getLongitude()).replace(",", ".");
        getSavedCoordinateComparison(formattedLocation);
    }

}
