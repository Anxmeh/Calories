package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class WaterView {

//    @SerializedName("userId")
//    @Expose
//    private long userId;
    @SerializedName("waterVolume")
    @Expose
    private int waterVolume;
    @SerializedName("dateOfDrink")
    @Expose
    private Date dateOfDrink;

    public WaterView(int waterVolume, Date dateOfDrink) {
        //this.userId = userId;
        this.waterVolume = waterVolume;
        this.dateOfDrink = dateOfDrink;
    }

    public WaterView() {
    }


    public int getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    public Date getDateOfDrink() {
        return dateOfDrink;
    }

    public void setDateOfDrink(Date dateOfDrink) {
        this.dateOfDrink = dateOfDrink;
    }
}
