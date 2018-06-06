package com.example.rojsa.weatherfromaccu.ui.search;

import com.example.rojsa.weatherfromaccu.LiveCicle;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySeachModel;

import java.util.List;

public interface CitySearchContract {
    interface View {
        void onSuccess(List<CitySeachModel> list);

        void onError(String msg);

    }

    interface Presenter extends LiveCicle<View> {
        void getCityList(String city);
        void saveCity(String id, String city);
    }
}

