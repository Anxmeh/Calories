package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetWaterTimeView {
    @SerializedName("hour")
    @Expose
    private int hour;
    @SerializedName("minute")
    @Expose
    private int minute;

    public SetWaterTimeView(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public SetWaterTimeView() {
    }
}
