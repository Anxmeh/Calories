package com.example.caloriescounter.models;

import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyXAxisValueFormatter  extends ValueFormatter {


    public String getXValue(String dateInMillisecons, int index, ViewPortHandler viewPortHandler) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");

            return sdf.format(new Date(Long.parseLong(dateInMillisecons)));

        } catch (Exception e) {

            return dateInMillisecons;
        }

    }
}

