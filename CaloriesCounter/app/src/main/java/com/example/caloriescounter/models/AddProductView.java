package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProductView {

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("protein")
    @Expose
    public double protein;
    @SerializedName("fat")
    @Expose
    public double fat;
    @SerializedName("carbohydrate")
    @Expose
    public double carbohydrate;
    @SerializedName("calories")
    @Expose
    public double calories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }
}
