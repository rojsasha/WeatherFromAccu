package com.example.rojsa.weatherfromaccu.ui.main;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rojsa on 13.04.2018.
 */

public class ForeCastAdapter extends ArrayAdapter {
    private List<DailyForecast> list;
    private Context context;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DailyForecast model = (DailyForecast) getItem(position);
        assert model != null;
        String url;
        if (model.getDay().getIcon() < 10) {
            url = String.format(Constants.URL_IMAGE_ZERO, model.getDay().getIcon());
        } else {
            url = String.format(Constants.URL_IMAGE_ONE, model.getDay().getIcon());
        }


        Picasso.get().load(url).into(holder.imageView);
        switch (position) {
            case 0:
                holder.tvDate.setText("Сегодня");
                break;
            case 1:
                holder.tvDate.setText("Завтра");
                break;
            default:
                holder.tvDate.setText(formatData(model.getDate()));
                break;
        }

        holder.tvTemp.setText(model.getTemperature().getMaximum().getValue() + "/" + model.getTemperature().getMinimum().getValue());
        return convertView;
    }

    private String formatData(String data) {
        String inputPattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        String outputPattern = "EEE";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());
        Date date;
        String str = null;
        try {
            date = inputFormat.parse(data);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    class ViewHolder {
        TextView tvDate, tvTemp;
        ImageView imageView;

        public ViewHolder(View view) {
            tvDate = view.findViewById(R.id.tvDate);
            imageView = view.findViewById(R.id.imgWeather);
            tvTemp = view.findViewById(R.id.tvTempDayNight);
        }

    }
}

