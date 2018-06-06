package com.example.rojsa.weatherfromaccu.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.rojsa.weatherfromaccu.R;;
import com.example.rojsa.weatherfromaccu.WeatherApplication;
import com.example.rojsa.weatherfromaccu.data.StringResourses;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;
import com.example.rojsa.weatherfromaccu.ui.search.CitySearchActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener, MainContract.View {
    private TextView tvTemperature, tvCity, tvDate;
    private String mKeyCity, mCity;
    private ListView listView;
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPresenter = new MainPresenter(WeatherApplication.get(this).getService(),
                new StringResourses(this));
        mPresenter.bind(this);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvCity = findViewById(R.id.tvCity);
        tvDate = findViewById(R.id.tvDate);
        listView = findViewById(R.id.listView);

        ImageButton btnSetCity = findViewById(R.id.btnSetCity);

        btnSetCity.setOnClickListener(this);
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
        mPresenter.getWeatherCurrent(mKeyCity);
        mPresenter.getWeatherForecast(mKeyCity);

    }

    @Override
    protected void onStop() {
        super.onStop();
        getSavedCity();
    }

    @Override
    public void onSuccessCurrentWeather(CurrentModel model) {
        tvTemperature.setText(String.valueOf(model.getTemperature().getMetric().getValue()));
    }

    @Override
    public void onSuccessForecastWeather(ForecastModel model) {
        tvTemperature.setText(String.valueOf(model.getDailyForecasts().get(0).getTemperature().getMaximum().getValue()));
        listView.setAdapter(new ForeCastAdapter(this, model.getDailyForecasts()));
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }
}