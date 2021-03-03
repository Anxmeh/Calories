package com.example.caloriescounter.models;

import android.util.Log;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        Log.e("in formatter, value", Float.toString(value));
        Log.e("in formatt, value long", Long.toString((long)value));
        // Convert float value to date string
        // Convert from seconds back to milliseconds to format time  to show to the user
      //  long emissionsMilliSince1970Time = ((long) value) * 1000;

        // Show time in local version
       // Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
       // DateFormat dateTimeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

       // Log.e("in formatter", timeMilliseconds.toString());
       long milis =  TimeUnit.DAYS.toMillis((long)value);

        Date date1 = new Date(milis);
        Date date = new Date((long)value);
        Log.e("in formatter date", date.toString());
        Log.e("in formatter date1", date1.toString());
        //Specify the format you'd like
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
        return sdf.format(date1);




       // return dateTimeFormat.format(timeMilliseconds);





    }
}
