package com.example.rojsa.weatherfromaccu.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.WeatherApplication;
import com.example.rojsa.weatherfromaccu.data.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySeachModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText etCityInput;
    private Button btnOkSearchCity;
    private ListView listViewCity;
    private WeatherInterface service;
    private List<CitySeachModel> saveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        etCityInput = findViewById(R.id.etCityInput);
        btnOkSearchCity = findViewById(R.id.btnOkSearchCity);
        listViewCity = findViewById(R.id.listViewCity);
        service = WeatherApplication.get(this).getService();

        btnOkSearchCity.setOnClickListener(this);

        listViewCity.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (etCityInput.getText().toString().length() < 2) {
            Toast.makeText(this, "Введите больше инормации", Toast.LENGTH_SHORT).show();
        } else {
            getCityList();
        }
    }

    private void getCityList() {

        Call<List<CitySeachModel>> cityModel = service.getCitySearch(getString(R.string.api_key2),
                etCityInput.getText().toString(),
                "ru-RU");

        cityModel.enqueue(new Callback<List<CitySeachModel>>() {
            @Override
            public void onResponse(Call<List<CitySeachModel>> call, Response<List<CitySeachModel>> response) {
                List<CitySeachModel> list = response.body();
                saveList = list;
                Toast.makeText(getApplicationContext(), "нашлось", Toast.LENGTH_SHORT).show();
                CitySearchAdapter adapter = new CitySearchAdapter(getApplicationContext(), list);
                listViewCity.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<CitySeachModel>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("id", saveList.get(i).getKey());
        intent.putExtra("city",saveList.get(i).getLocalizedName());
        setResult(RESULT_OK,intent);
        finish();

    }
}
