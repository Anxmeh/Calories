package com.example.caloriescounter.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.caloriescounter.R;

public class SessionManager {
    private static SessionManager instance = null;
    private final SharedPreferences prefs;
    private final String USER_TOKEN = "user_token";
    private final String USER_LOGIN = "user_login";
    private final String USER_NAME = "user_name";
    public boolean isLogged;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        isLogged = fetchAuthToken() != null;
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveJWTToken(String token) {
        SharedPreferences.Editor edit = prefs.edit();
        try {
            edit.putString(USER_TOKEN, token);
            Log.i(USER_TOKEN, token);
            edit.apply();
            isLogged = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fetchAuthToken() {
        return prefs.getString(USER_TOKEN, null);
    }

    public String fetchAuthTokenWithBearer() {
        return "Bearer " + prefs.getString(USER_TOKEN, null);
    }

    public void saveUserName(String name) {
        SharedPreferences.Editor edit = prefs.edit();
        try {
            edit.putString(USER_NAME, name);
            Log.i(USER_NAME, name);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fetchUserName() {
        return prefs.getString(USER_NAME, null);
    }

    public void saveUserLogin(String login) {
        SharedPreferences.Editor edit = prefs.edit();
        try {
            edit.putString(USER_LOGIN, login);
            Log.i(USER_LOGIN, login);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fetchUserLogin() {
        return prefs.getString(USER_LOGIN, null);
    }

    public void logout() {
        SharedPreferences.Editor edit = prefs.edit();
        try {
            edit.clear();
            edit.apply();
            isLogged = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}