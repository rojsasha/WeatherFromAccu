package com.example.rojsa.weatherfromaccu.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.models.city_search.CitySearchModel;

import java.util.List;

/**
 * Created by rojsa on 13.04.2018.
 */

public class CitySearchAdapter extends ArrayAdapter {
    public CitySearchAdapter(@NonNull Context context, List<CitySearchModel> list) {
        super(context, 0,list);
        Context context1 = context;
        List<CitySearchModel> list1 = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_city_search,parent,false);
            holder.tvCityList = convertView.findViewById(R.id.tvCityList);
            holder.tvCityRegion = convertView.findViewById(R.id.tvCityRegion);

            convertView.setTag(holder);
        }else{
         holder = (ViewHolder) convertView.getTag();
        }
        CitySearchModel model = (CitySearchModel) getItem(position);
        assert model != null;
        holder.tvCityList.setText(model.getLocalizedName());
        holder.tvCityRegion.setText(model.getAdministrativeArea().getLocalizedName() + ", " + model.getCountry().getLocalizedName());
        return convertView;
    }
    class ViewHolder{
        TextView tvCityList,tvCityRegion;
    }
}
