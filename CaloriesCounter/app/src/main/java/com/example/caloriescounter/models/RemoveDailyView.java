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

    public RemoveDailyView(long productId, Date dateOfMeal) {
        this.productId = productId;
        this.dateOfMeal = dateOfMeal;
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
}
