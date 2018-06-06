package com.example.rojsa.weatherfromaccu.ui.main;

import android.support.annotation.NonNull;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.StringResourses;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private WeatherInterface mService;
    private StringResourses mGetString;

    public MainPresenter(WeatherInterface service, StringResourses getString) {
        mService = service;
        mGetString = getString;
    }

    @Override
    public void getWeatherCurrent(String keyCity) {
        if (keyCity== null) {
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
        else {
            mView.onError("нету данных");
        }
    }

    @Override
    public void getWeatherForecast(String keyCity) {
        if (keyCity != null) {
            mService.getCityForecastFiveDays(keyCity, mGetString.getString(R.string.api_key2), "ru-RU",
                    true, true)
                    .enqueue(new Callback<ForecastModel>() {
                        @Override
                        public void onResponse(@NonNull Call<ForecastModel> call, Response<ForecastModel> response) {
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
        }else {
            mView.onError("нету данных");
        }

    }

    @Override
    public void bind(MainContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }
}
