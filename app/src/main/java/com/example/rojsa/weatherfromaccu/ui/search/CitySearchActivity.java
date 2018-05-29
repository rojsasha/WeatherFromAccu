package com.example.rojsa.weatherfromaccu.ui.search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.StringResourses;
import com.example.rojsa.weatherfromaccu.data.WeatherApplication;
import com.example.rojsa.weatherfromaccu.data.WeatherInterface;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySeachModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CitySearchActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CitySearchContract.View {
    private EditText etCityInput;
    private Button btnOkSearchCity;
    private ListView listViewCity;
    private CitySearchPresenter mPresenter;

    private List<CitySeachModel> saveList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_city_search);

        etCityInput = findViewById(R.id.etCityInput);
        mPresenter = new CitySearchPresenter(WeatherApplication.get(this).getService()
                , new StringResourses(this), this);

        btnOkSearchCity = findViewById(R.id.btnOkSearchCity);
        listViewCity = findViewById(R.id.listViewCity);

        btnOkSearchCity.setOnClickListener(this);

        listViewCity.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        mPresenter.getCityList(etCityInput.getText().toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra("id", saveList.get(i).getKey());
        intent.putExtra("city", saveList.get(i).getLocalizedName());
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void onSuccess(List<CitySeachModel> list) {
        listViewCity.setAdapter(new CitySearchAdapter(this, saveList = list));
    }

    @Override
    public void onError(String msg) {

    }
}
