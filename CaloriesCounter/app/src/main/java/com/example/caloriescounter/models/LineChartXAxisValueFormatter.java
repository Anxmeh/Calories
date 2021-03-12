package com.example.caloriescounter.models;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {

    @Override
    public String getFormattedValue(float value) {
        long milis = TimeUnit.DAYS.toMillis((long) value);
        Date date = new Date(milis);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
        return sdf.format(date);
    }
}
