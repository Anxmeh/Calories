package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dish {

    @SerializedName("dishCalories")
    @Expose
    private double DishCalories;

    @SerializedName("dishWeight")
    @Expose
    private double DishWeight;

    public Dish() {
    }

    public Dish(double dishCalories, double dishWeight) {
        DishCalories = dishCalories;
        DishWeight = dishWeight;
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
