package com.example.rojsa.weatherfromaccu.ui.main;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rojsa.weatherfromaccu.R;;
import com.example.rojsa.weatherfromaccu.WeatherApplication;
import com.example.rojsa.weatherfromaccu.data.StringResources;
import com.example.rojsa.weatherfromaccu.models.CurrentModel;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.ForecastModel;
import com.example.rojsa.weatherfromaccu.ui.base.BaseActivity;
import com.example.rojsa.weatherfromaccu.ui.search.CitySearchActivity;

import io.realm.Realm;

public class MainActivity extends BaseActivity implements OnClickListener, MainContract.View {
    private TextView tvTemperature, tvCity, tvTextWeather;
    private String mKeyCity, mCity;
    private ListView listView;
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressBar();

        mPresenter = new MainPresenter(WeatherApplication.get(this).getService(),
                WeatherApplication.get(this).getRealm(),
                new StringResources(this),
                (LocationManager) getSystemService(LOCATION_SERVICE));
        mPresenter.bind(this);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvCity = findViewById(R.id.tvCity);
        tvTextWeather = findViewById(R.id.tvTextWeather);
        listView = findViewById(R.id.listView);

        ImageButton btnSetCity = findViewById(R.id.btnSetCity);

        btnSetCity.setOnClickListener(this);
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
    }

    @Override
    public void onSuccessCurrentWeather(String temp, String weathetText) {
        tvTemperature.setText(String.valueOf(temp));
        tvTextWeather.setText(weathetText);
        dismissProgressBar();
    }

    @Override
    public void onSuccessLocationCurrentWeather(String city) {
        tvCity.setText(city);
    }

    @Override
    public void onSuccessForecastWeather(ForecastModel model) {
        listView.setAdapter(new ForeCastAdapter(this, model.getDailyForecasts()));
        dismissProgressBar();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg + "1", Toast.LENGTH_SHORT).show();
        dismissProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unbind();
    }
}