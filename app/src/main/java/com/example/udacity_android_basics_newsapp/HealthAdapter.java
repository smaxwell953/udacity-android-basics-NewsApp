package com.example.udacity_android_basics_newsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HealthAdapter extends ArrayAdapter<Health> {

    private static final String LOG_TAG = HealthAdapter.class.getSimpleName();

    public HealthAdapter(@NonNull Context context, List<Health> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_list_item, parent, false);
        }

        Health health = getItem(position);

        TextView title = convertView.findViewById(R.id.title);
        title.setText(health.getType());

        TextView author = convertView.findViewById(R.id.author);
        author.setText(health.getTitle());

        TextView date = convertView.findViewById(R.id.date);
        date.setText(changeDateToDays(health.getDate()));

        TextView time = convertView.findViewById(R.id.time);
        time.setText(changeDateToTime(health.getDate()));

        return convertView;
    }

    private String changeDateToDays(String date) {
        String days = null;

        try {
            Date dateOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
            days = new SimpleDateFormat("yyyy-MM-dd").format(dateOut);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "There was a problem while changing date - days");
        }

        return days;
    }

    private String changeDateToTime(String date) {
        String time = null;

        try {
            Date dateOut = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
            time = new SimpleDateFormat("H:mm:ss").format(dateOut);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "There was a problem while changing date - time");
        }

        return time;
    }
}