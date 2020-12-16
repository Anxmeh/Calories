package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DishIngredientsView implements Serializable {

    @SerializedName("ProductName")
    @Expose
    public String productName;
    @SerializedName("productprotein")
    @Expose
    private double productProtein;
    @SerializedName("productfat")
    @Expose
    private double productFat;
    @SerializedName("productcarbohydrate")
    @Expose
    private double productCarbohydrate;
    @SerializedName("productcalories")
    @Expose
    private double productCalories;
    @SerializedName("productweight")
    @Expose
    private double productWeight;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductProtein() {
        return productProtein;
    }

    public void setProductProtein(double protein) {
        this.productProtein = protein;
    }

    public double getProductFat() {
        return productFat;
    }

    public void setProductFat(double productFat) {
        this.productFat = productFat;
    }

    public double getProductCarbohydrate() {
        return productCarbohydrate;
    }

    public void setProductCarbohydrate(double productCarbohydrate) {
        this.productCarbohydrate = productCarbohydrate;
    }

    public double getProductCalories() {
        return productCalories;
    }

    public void setProductCalories(double productCalories) {
        this.productCalories = productCalories;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public DishIngredientsView(String productName, double productProtein, double productFat, double productCarbohydrate, double productCalories, double productWeight) {
        this.productName = productName;
        this.productProtein = productProtein;
        this.productFat = productFat;
        this.productCarbohydrate = productCarbohydrate;
        this.productCalories = productCalories;
        this.productWeight = productWeight;
    }

    public DishIngredientsView() {
    }
}
