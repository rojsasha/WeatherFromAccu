package com.example.rojsa.weatherfromaccu.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rojsa.weatherfromaccu.R;
import com.example.rojsa.weatherfromaccu.data.Constants;
import com.example.rojsa.weatherfromaccu.models.forecats_five_days.DailyForecast;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rojsa on 13.04.2018.
 */

public class ForeCastAdapter extends ArrayAdapter {
    List<DailyForecast> list;
    Context context;

    public ForeCastAdapter(@NonNull Context context, List<DailyForecast> list) {
        super(context, 0, list);
        this.list = list;
        this.context = context;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder.tvDate = convertView.findViewById(R.id.tvDate);
            holder.imageView = convertView.findViewById(R.id.imgWeather);
            holder.tvTemp = convertView.findViewById(R.id.tvTempDayNight);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DailyForecast model = (DailyForecast) getItem(position);
        assert model != null;
        String url;
        if (model.getDay().getIcon()<10){
            url = String.format(Constants.URL_IMAGE_ZERO, model.getDay().getIcon());
        }
        else {
            url = String.format(Constants.URL_IMAGE_ONE, model.getDay().getIcon());
        }


        Picasso.get().load(url).into(holder.imageView);
        holder.tvTemp.setText(model.getTemperature().getMaximum().getValue() + "/" + model.getTemperature().getMinimum().getValue());
        return convertView;
    }

    class ViewHolder {
        TextView tvDate, tvTemp;
        ImageView imageView;

    }
}

