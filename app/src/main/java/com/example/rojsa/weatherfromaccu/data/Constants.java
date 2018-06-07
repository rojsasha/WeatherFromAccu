package com.example.rojsa.weatherfromaccu.data;

/**
 * Created by rojsa on 07.04.2018.
 */

public class Constants {
    public static final String BASE_URL = "http://dataservice.accuweather.com/";
    public static final String URL_CITY_SEARCH = "locations/v1/cities/autocomplete";
    public static final String URL_CITY_SEARCH_GEOPOSITION = "locations/v1/cities/geoposition/search.json";
    public static final String URL_FORECAST = "forecasts/v1/daily/5day/";
    public static final String URL_IMAGE_ZERO = "https://developer.accuweather.com/sites/default/files/0%1s-s.png";
    public static final String URL_IMAGE_ONE = "https://developer.accuweather.com/sites/default/files/%1s-s.png";

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 100;
}
