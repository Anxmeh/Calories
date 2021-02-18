package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterTimeView {
    @SerializedName("beginHour")
    @Expose
    private int beginHour;
    @SerializedName("beginMinute")
    @Expose
    private int beginMinute;
    @SerializedName("endHour")
    @Expose
    private int endHour;
    @SerializedName("endMinute")
    @Expose
    private int endMinute;

    public int getBeginHour() {
        return beginHour;
    }

    public void setBeginHour(int beginHour) {
        this.beginHour = beginHour;
    }

    public int getBeginMinute() {
        return beginMinute;
    }

    public void setBeginMinute(int beginMinute) {
        this.beginMinute = beginMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public WaterTimeView() {
    }

    public WaterTimeView(int beginHour, int beginMinute, int endHour, int endMinute) {
        this.beginHour = beginHour;
        this.beginMinute = beginMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
}
