package com.example.caloriescounter.network.interceptors;

import android.util.Log;

import com.example.caloriescounter.application.HomeApplication;
import com.example.caloriescounter.network.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JWTInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HomeApplication context = (HomeApplication)HomeApplication.getAppContext();
        String token = SessionManager.getInstance(context).fetchAuthToken();
        if(token!=null && !token.isEmpty())
        {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer "+ token)
                    .build();
            Log.e("Send JWT Token", token);
            return chain.proceed(newRequest);
        }

        Request newRequest = originalRequest.newBuilder()
                .build();
        return chain.proceed(newRequest);
    }
}