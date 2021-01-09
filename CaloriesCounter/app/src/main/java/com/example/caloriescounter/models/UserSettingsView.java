package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserSettingsView {
    @SerializedName("age")
    @Expose
    private double age;
    @SerializedName("weight")
    @Expose
    private double weight;
    @SerializedName("height")
    @Expose
    private double height;
    @SerializedName("chest")
    @Expose
    private double chest;
    @SerializedName("waist")
    @Expose
    private double waist;
    @SerializedName("hips")
    @Expose
    private double hips;
    @SerializedName("hip")
    @Expose
    private double hip;
    @SerializedName("shin")
    @Expose
    private double shin;
    @SerializedName("wrist")
    @Expose
    private double wrist;
    @SerializedName("forearm")
    @Expose
    private double forearm;
    @SerializedName("neck")
    @Expose
    private double neck;
    @SerializedName("calories")
    @Expose
    private double calories;
    @SerializedName("bmi")
    @Expose
    private double bmi;
    @SerializedName("fatPercentage")
    @Expose
    private double fatPercentage;
    @SerializedName("sex")
    @Expose
    private boolean sex;
    @SerializedName("activity")
    @Expose
    private double activity;
    @SerializedName("userCalories")
    @Expose
    private double userCalories;
    @SerializedName("userProtein")
    @Expose
    private double userProtein;
    @SerializedName("userFat")
    @Expose
    private double userFat;
    @SerializedName("userCarbohydrate")
    @Expose
    private double userCarbohydrate;

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getChest() {
        return chest;
    }

    public void setChest(double chest) {
        this.chest = chest;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getHips() {
        return hips;
    }

    public void setHips(double hips) {
        this.hips = hips;
    }

    public double getHip() {
        return hip;
    }

    public void setHip(double hip) {
        this.hip = hip;
    }

    public double getShin() {
        return shin;
    }

    public void setShin(double shin) {
        this.shin = shin;
    }

    public double getWrist() {
        return wrist;
    }

    public void setWrist(double wrist) {
        this.wrist = wrist;
    }

    public double getForearm() {
        return forearm;
    }

    public void setForearm(double forearm) {
        this.forearm = forearm;
    }

    public double getNeck() {
        return neck;
    }

    public void setNeck(double neck) {
        this.neck = neck;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public double getFatPercentage() {
        return fatPercentage;
    }

    public void setFatPercentage(double fatPercentage) {
        this.fatPercentage = fatPercentage;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public double getActivity() {
        return activity;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public double getUserCalories() {
        return userCalories;
    }

    public void setUserCalories(double userCalories) {
        this.userCalories = userCalories;
    }

    public double getUserProtein() {
        return userProtein;
    }

    public void setUserProtein(double userProtein) {
        this.userProtein = userProtein;
    }

    public double getUserFat() {
        return userFat;
    }

    public void setUserFat(double userFat) {
        this.userFat = userFat;
    }

    public double getUserCarbohydrate() {
        return userCarbohydrate;
    }

    public void setUserCarbohydrate(double userCarbohydrate) {
        this.userCarbohydrate = userCarbohydrate;
    }
}
