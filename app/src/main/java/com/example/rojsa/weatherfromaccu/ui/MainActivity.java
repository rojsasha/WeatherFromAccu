package com.example.rojsa.weatherfromaccu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.Constants;
import com.example.rojsa.weatherfromaccu.data.WeatherApplication;
import com.example.rojsa.weatherfromaccu.data.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.DailyForecast;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private WeatherInterface service;
    private TextView tvTemperature, tvCity, tvDate;
    private String mKeyCity, mCity;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = WeatherApplication.get(this).getService();
        tvTemperature = findViewById(R.id.tvTemperature);
        tvCity = findViewById(R.id.tvCity);
        tvDate = findViewById(R.id.tvDate);
        listView = findViewById(R.id.listView);
        String str = "2018-04-18T07:00+06:00";
      DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
      DateTime dateTime = new DateTime(dateTimeFormatter.parseDateTime(str));
      String format = sdf.format(dateTime);
      tvTemperature.setText(format);
        ImageButton btnSetCity = findViewById(R.id.btnSetCity);

        btnSetCity.setOnClickListener(this);
//        getWeatherCurrent();
//        getWeatherForecast();


    }

    private void getSavedCity() {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        if (preferences != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("id", mKeyCity);
            editor.putString("city", mCity);
            editor.apply();
        } else {
            mKeyCity = preferences.getString("id", "Выберите город");
            mCity = preferences.getString("city", "Выберите город");
        }
    }

    private void getWeatherForecast() {
        if (mKeyCity == null) {
            return;
        }
        Call<ForecastModel> json = service.getCityForecastFiveDays(mKeyCity, getString(R.string.api_key2), "ru-RU",
                true, true);
        json.enqueue(new Callback<ForecastModel>() {
            @Override
            public void onResponse(@NonNull Call<ForecastModel> call, @NonNull Response<ForecastModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    ForecastModel model = response.body();
                    tvTemperature.setText(String.valueOf(model.getDailyForecasts().get(0).getTemperature().getMaximum().getValue()));
                    ForeCastAdapter adapter = new ForeCastAdapter(getApplicationContext(), model.getDailyForecasts());
                    listView.setAdapter(adapter);


                }


            }

            @Override
            public void onFailure(@NonNull Call<ForecastModel> call, @NonNull Throwable t) {

            }
        });

    }
//    private void getWeatherCurrent() {
//        if (keyCity == null){
//            return;
//        }
//
//        Call<List<CurrentModel>> currentModelCall = service.getCurrentWeather(keyCity, getString(R.string.api_key1));
//        currentModelCall.enqueue(new Callback<List<CurrentModel>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<CurrentModel>> call, @NonNull Response<List<CurrentModel>> response) {
//                List<CurrentModel> list = response.body();
//                CurrentModel model = list.get(0);
//                tvTemperature.setText(String.valueOf(model.getTemperature().getMetric().getValue()));
//                String url = String.format("https://developer.accuweather.com/sites/default/files/0%1s-s.png",model.getWeatherIcon());
//                Picasso.get().load(url).into(imgWeather);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<CurrentModel>> call, @NonNull Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, CitySearchActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        mKeyCity = data.getStringExtra("id");
        mCity = data.getStringExtra("city");
        tvCity.setText(mCity);

//        getWeatherCurrent();
//        getWeatherForecast();


    }

    @Override
    protected void onStop() {
        super.onStop();
        getSavedCity();
    }
}