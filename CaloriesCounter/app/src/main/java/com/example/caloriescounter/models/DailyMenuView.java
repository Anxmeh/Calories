package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class DailyMenuView implements Serializable {

    @SerializedName("userId")
    @Expose
    private long userId;

    @SerializedName("productId")
    @Expose
    private long productId;

    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productWeight")
    @Expose
    private double productWeight;
    @SerializedName("productProtein")
    @Expose
    private double productProtein;
    @SerializedName("productCarbohydrate")
    @Expose
    private double productCarbohydrate;
    @SerializedName("productFat")
    @Expose
    private double productFat;
    @SerializedName("productCalories")
    @Expose
    private double productCalories;
    @SerializedName("dateOfMeal")
    @Expose
    private Date dateOfMeal;

    public DailyMenuView() {
    }

    public DailyMenuView(long userId, long productId, String productName, double productWeight,
                         double productProtein, double productCarbohydrate,
                         double productFat, double productCalories, Date dateOfMeal ) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productWeight = productWeight;
        this.productProtein = productProtein;
        this.productCarbohydrate = productCarbohydrate;
        this.productFat = productFat;
        this.productCalories = productCalories;
        this.dateOfMeal = dateOfMeal;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public double getProductProtein() {
        return productProtein;
    }

    public void setProductProtein(double productProtein) {
        this.productProtein = productProtein;
    }

    public double getProductCarbohydrate() {
        return productCarbohydrate;
    }

    public void setProductCarbohydrate(double productCarbohydrate) {
        this.productCarbohydrate = productCarbohydrate;
    }

    public double getProductFat() {
        return productFat;
    }

    public void setProductFat(double productFat) {
        this.productFat = productFat;
    }

    public double getProductCalories() {
        return productCalories;
    }

    public void setProductCalories(double productCalories) {
        this.productCalories = productCalories;
    }
}
