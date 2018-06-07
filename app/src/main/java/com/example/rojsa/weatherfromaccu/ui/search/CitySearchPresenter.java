package com.example.rojsa.weatherfromaccu.ui.search;

import android.support.annotation.NonNull;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.StringResources;
import com.example.rojsa.weatherfromaccu.data.db.SaveCityModel;
import com.example.rojsa.weatherfromaccu.data.internet.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySearchModel;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchPresenter implements CitySearchContract.Presenter {
    private WeatherInterface mService;
    private StringResources mGetString;
    private CitySearchContract.View mView;


    public CitySearchPresenter(WeatherInterface service, StringResources getString) {
        mService = service;
        mGetString = getString;
    }


    @Override
    public void getCityList(String city) {
        Call<List<CitySearchModel>> cityModel = mService.getCitySearch(mGetString.getString(R.string.api_key2),
                city,
                "ru-RU");

        cityModel.enqueue(new Callback<List<CitySearchModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CitySearchModel>> call, Response<List<CitySearchModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mView.onSuccess(response.body());

                } else {
                    mView.onError(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CitySearchModel>> call, @NonNull Throwable t) {
                mView.onError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void saveCity(String id, String city) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        SaveCityModel model = realm.createObject(SaveCityModel.class);
        model.setIdCity(id);
        model.setCityName(city);
        realm.commitTransaction();
    }

    @Override
    public void bind(CitySearchContract.View view) {
        mView = view;
    }

    @Override
    public void unbind() {
        mView = null;
    }
}
