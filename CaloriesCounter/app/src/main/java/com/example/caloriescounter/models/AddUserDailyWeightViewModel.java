package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class AddUserDailyWeightViewModel {
   @SerializedName("weight")
    @Expose
    private double weight;

    @SerializedName("dateOfWeight")
    @Expose
    private Date dateOfWeight;

    public AddUserDailyWeightViewModel() {
    }

    public AddUserDailyWeightViewModel(double weight, Date dateOfWeight) {
        this.weight = weight;
        this.dateOfWeight = dateOfWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDateOfWeight() {
        return dateOfWeight;
    }

    public void setDateOfWeight(Date dateOfWeight) {
        this.dateOfWeight = dateOfWeight;
    }
}
