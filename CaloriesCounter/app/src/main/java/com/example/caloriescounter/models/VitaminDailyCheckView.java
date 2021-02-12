package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class VitaminDailyCheckView implements Serializable {
    @SerializedName("vitaminId")
    @Expose
    private long vitaminId;
    @SerializedName("dateOfVitamin")
    @Expose
    private Date dateOfVitamin;
    @SerializedName("isTaken")
    @Expose
    private boolean isTaken;

    public VitaminDailyCheckView(long vitaminId, Date dateOfVitamin, boolean isTaken) {
        this.vitaminId = vitaminId;
        this.dateOfVitamin = dateOfVitamin;
        this.isTaken = isTaken;
    }

    public long getVitaminId() {
        return vitaminId;
    }

    public void setVitaminId(long vitaminId) {
        this.vitaminId = vitaminId;
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

    public VitaminDailyCheckView() {
    }
}
