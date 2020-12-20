package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("dishName")
    @Expose
    private String DishName;

    @SerializedName("dishProtein")
    @Expose
    private double DishProtein;

    @SerializedName("dishCarbohydrate")
    @Expose
    private double DishCarbohydrate;

    @SerializedName("dishFat")
    @Expose
    private double DishFat;

    @SerializedName("dishCalories")
    @Expose
    private double DishCalories;

    @SerializedName("dishWeight")
    @Expose
    private double DishWeight;

    public Dish() {
    }

    public Dish(String dishName, double dishProtein, double dishCarbohydrate, double dishFat, double dishCalories, double dishWeight) {
        DishName = dishName;
        DishProtein = dishProtein;
        DishCarbohydrate = dishCarbohydrate;
        DishFat = dishFat;
        DishCalories = dishCalories;
        DishWeight = dishWeight;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public double getDishProtein() {
        return DishProtein;
    }

    public void setDishProtein(double dishProtein) {
        DishProtein = dishProtein;
    }

    public double getDishCarbohydrate() {
        return DishCarbohydrate;
    }

    public void setDishCarbohydrate(double dishCarbohydrate) {
        DishCarbohydrate = dishCarbohydrate;
    }

    public double getDishFat() {
        return DishFat;
    }

    public void setDishFat(double dishFat) {
        DishFat = dishFat;
    }

    public double getDishCalories() {
        return DishCalories;
    }

    public void setDishCalories(double dishCalories) {
        DishCalories = dishCalories;
    }

    public double getDishWeight() {
        return DishWeight;
    }

    public void setDishWeight(double dishWeight) {
        DishWeight = dishWeight;
    }
}
