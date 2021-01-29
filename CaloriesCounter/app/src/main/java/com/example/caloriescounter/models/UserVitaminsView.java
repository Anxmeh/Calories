package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserVitaminsView {
    @SerializedName("vitaminId")
    @Expose
    private long vitaminId;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("vitaminName")
    @Expose
    private String vitaminName;

    public long getVitaminId() {
        return vitaminId;
    }

    public void setVitaminId(long vitaminId) {
        this.vitaminId = vitaminId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getVitaminName() {
        return vitaminName;
    }

    public void setVitaminName(String vitaminName) {
        this.vitaminName = vitaminName;
    }

    public UserVitaminsView(long vitaminId, int amount, String vitaminName) {
        this.vitaminId = vitaminId;
        this.amount = amount;
        this.vitaminName = vitaminName;
    }

    public UserVitaminsView() {
    }
}
