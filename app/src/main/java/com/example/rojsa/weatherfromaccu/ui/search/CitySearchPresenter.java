package com.example.rojsa.weatherfromaccu.ui.search;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.StringResourses;
import com.example.rojsa.weatherfromaccu.data.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySeachModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchPresenter implements CitySearchContract.Presenter {
    private WeatherInterface mService;
    private StringResourses mGetString;
    private CitySearchContract.View mView;

    public CitySearchPresenter(WeatherInterface service, StringResourses getString,
                               CitySearchContract.View view) {
        mService = service;
        mGetString = getString;
        mView = view;
    }

    @Override
    public void getCityList(String city) {
        Call<List<CitySeachModel>> cityModel = mService.getCitySearch(mGetString.getString(R.string.api_key2),
                city,
                "ru-RU");

        cityModel.enqueue(new Callback<List<CitySeachModel>>() {
            @Override
            public void onResponse(Call<List<CitySeachModel>> call, Response<List<CitySeachModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mView.onSuccess(response.body());
                } else {
                    mView.onError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CitySeachModel>> call, Throwable t) {
                mView.onError(t.getLocalizedMessage());
            }
        });
    }
}
