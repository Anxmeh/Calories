package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ingredients implements Serializable {

    @SerializedName("productName")
    @Expose
    private String productName;

    @SerializedName("productProtein")
    @Expose
    private double productProtein;

    @SerializedName("productFat")
    @Expose
    private double productFat;

    @SerializedName("productCarbohydrate")
    @Expose
    private double productCarbohydrate;

    @SerializedName("productCalories")
    @Expose
    private double productCalories;

    @SerializedName("productWeight")
    @Expose
    private double productWeight;

    public Ingredients(String productName, double productProtein, double productFat, double productCarbohydrate, double productCalories, double productWeight) {
        this.productName = productName;
        this.productProtein = productProtein;
        this.productFat = productFat;
        this.productCarbohydrate = productCarbohydrate;
        this.productCalories = productCalories;
        this.productWeight = productWeight;
    }

    public Ingredients() {
    }

    public String getProductName() {
        return productName;
    }

    public double getProductProtein() {
        return productProtein;
    }

    public double getProductFat() {
        return productFat;
    }

    public double getProductCarbohydrate() {
        return productCarbohydrate;
    }

    public double getProductCalories() {
        return productCalories;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductProtein(double productProtein) {
        this.productProtein = productProtein;
    }

    public void setProductFat(double productFat) {
        this.productFat = productFat;
    }

    public void setProductCarbohydrate(double productCarbohydrate) {
        this.productCarbohydrate = productCarbohydrate;
    }

    public void setProductCalories(double productCalories) {
        this.productCalories = productCalories;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }
}
