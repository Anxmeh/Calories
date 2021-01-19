package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class WaterSettingsView {
    @SerializedName("begin")
    @Expose
    private int begin;
    @SerializedName("end")
    @Expose
    private int end;

    public WaterSettingsView(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public WaterSettingsView() {
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
