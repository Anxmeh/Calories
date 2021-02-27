package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UserDailyWeight  implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("weight")
    @Expose
    private double weight;

    @SerializedName("dateOfWeight")
    @Expose
    private Date dateOfWeight;

    public UserDailyWeight(long id, long userId, double weight, Date dateOfWeight) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.dateOfWeight = dateOfWeight;
    }

    public UserDailyWeight() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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
