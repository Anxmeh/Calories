package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class RemoveDailyView implements Serializable {
    @SerializedName("productId")
    @Expose
    private long productId;

    @SerializedName("dateOfMeal")
    @Expose
    private Date dateOfMeal;

    @SerializedName("productWeight")
    @Expose
    private double productWeight;

    public RemoveDailyView(long productId, Date dateOfMeal, double productWeight) {
        this.productId = productId;
        this.dateOfMeal = dateOfMeal;
        this.productWeight = productWeight;
    }

    public RemoveDailyView() {
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public Date getDateOfMeal() {
        return dateOfMeal;
    }

    public void setDateOfMeal(Date dateOfMeal) {
        this.dateOfMeal = dateOfMeal;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }
}
