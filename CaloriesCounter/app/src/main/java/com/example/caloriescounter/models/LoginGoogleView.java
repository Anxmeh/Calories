package com.example.caloriescounter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginGoogleView {
    @SerializedName("idtoken")
    @Expose
    private String idtoken;

    public String getIdtoken() {
        return idtoken;
    }

    public void setIdtoken(String idtoken) {
        this.idtoken = idtoken;
    }

    public LoginGoogleView() {
    }

    public LoginGoogleView(String idtoken) {
        this.idtoken = idtoken;
    }
}
