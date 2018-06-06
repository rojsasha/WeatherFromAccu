package com.example.rojsa.weatherfromaccu.ui.main;

import com.example.rojsa.weatherfromaccu.LiveCicle;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;

public interface MainContract {
    interface View {
        void onSuccessCurrentWeather(CurrentModel model);

        void onSuccessForecastWeather(ForecastModel model);

        void onError(String msg);


    }

    interface Presenter extends LiveCicle<View> {
        void getWeatherCurrent(String keyCity);
        void getWeatherForecast(String keyCity);


    }
}
