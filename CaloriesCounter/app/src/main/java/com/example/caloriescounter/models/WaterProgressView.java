package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaterProgressView {
    @SerializedName("waterVolume")
    @Expose
    private int waterVolume;

    public int getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    public WaterProgressView() {
    }

    public WaterProgressView(int waterVolume) {
        this.waterVolume = waterVolume;
    }
}
