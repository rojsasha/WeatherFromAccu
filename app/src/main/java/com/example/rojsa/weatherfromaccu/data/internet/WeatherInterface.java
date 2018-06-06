package com.example.rojsa.weatherfromaccu.data.internet;

import com.example.rojsa.weatherfromaccu.data.Constants;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySearchModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rojsa on 07.04.2018.
 */

public interface WeatherInterface {
    @GET("currentconditions/v1/{name}")
    Call<List<CurrentModel>> getCurrentWeather(@Path("name") String name,
                                               @Query("apikey") String apiKey
    );

//    @Query("language") String lang

    @GET(Constants.URL_CITY_SEARCH)
    Call<List<CitySearchModel>> getCitySearch(@Query("apikey") String apiKey,
                                              @Query("q") String city,
                                              @Query("language") String lang);

    @GET(Constants.URL_FORECAST + "{name}")
    Call<ForecastModel> getCityForecastFiveDays(@Path("name") String name,
                                                @Query("apikey") String apiKey,
                                                @Query("language") String lang,
                                                @Query("details") boolean details,
                                                @Query("metric") boolean metric);

}