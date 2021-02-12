package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class UserVitaminsDailyView implements Serializable {
    @SerializedName("userId")
    @Expose
    private long userId;
    @SerializedName("vitaminId")
    @Expose
    private long vitaminId;
    @SerializedName("vitaminName")
    @Expose
    private String vitaminName;
    @SerializedName("amount")
    @Expose
    private int amount;

    @SerializedName("dateOfVitamin")
    @Expose
    private Date dateOfVitamin;
    @SerializedName("isTaken")
    @Expose
    private boolean isTaken;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getVitaminName() {
        return vitaminName;
    }

    public void setVitaminName(String vitaminName) {
        this.vitaminName = vitaminName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDateOfVitamin() {
        return dateOfVitamin;
    }

    public void setDateOfVitamin(Date dateOfVitamin) {
        this.dateOfVitamin = dateOfVitamin;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public long getVitaminId() {
        return vitaminId;
    }

    public void setVitaminId(long vitaminId) {
        this.vitaminId = vitaminId;
    }

    public UserVitaminsDailyView(long userId, long vitaminId, String vitaminName, int amount, Date dateOfVitamin, boolean isTaken) {
        this.userId = userId;
        this.vitaminId = vitaminId;
        this.vitaminName = vitaminName;
        this.amount = amount;
        this.dateOfVitamin = dateOfVitamin;
        this.isTaken = isTaken;
    }

    public UserVitaminsDailyView() {
    }
}
