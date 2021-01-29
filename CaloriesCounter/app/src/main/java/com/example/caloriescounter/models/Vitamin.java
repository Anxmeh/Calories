package com.example.caloriescounter.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vitamin implements Serializable {
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("vitaminName")
    @Expose
    private String vitaminName;

    public Vitamin() {}

    public Vitamin(long id, String vitaminName) {
        this.id = id;
        this.vitaminName = vitaminName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVitaminName() {
        return vitaminName;
    }

    public void setVitaminName(String vitaminName) {
        this.vitaminName = vitaminName;
    }

    @Override
    public String toString() {
        return new String(Long.toString(this.id) +  this.vitaminName);
               // this.vitaminName;
    }
}
