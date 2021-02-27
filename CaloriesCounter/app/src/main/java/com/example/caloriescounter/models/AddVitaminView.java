package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVitaminView {
    @SerializedName("vitaminName")
    @Expose
    public String vitaminName;

    public AddVitaminView() {
    }

    public AddVitaminView(String vitaminName) {
        this.vitaminName = vitaminName;
    }

    public String getVitaminName() {
        return vitaminName;
    }

    public void setVitaminName(String vitaminName) {
        this.vitaminName = vitaminName;
    }
}
