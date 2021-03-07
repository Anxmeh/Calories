package com.example.caloriescounter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.volley.toolbox.NetworkImageView;

import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private SessionManager sessionManager;
    private TextView textView;
    private UserView userProfile;
    private final String BASE_URL = NetworkService.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_main);
        this.getSupportActionBar().setTitle("Головна");

        ImageRequester imageRequester = ImageRequester.getInstance();
        NetworkImageView editImage = findViewById(R.id.chooseImage);
        imageRequester.setImageFromUrl(editImage, NetworkService.getBaseUrl() + "/images/testAvatarHen.jpg");
        sessionManager = SessionManager.getInstance(this);
        textView = findViewById(R.id.textMainActivity);

        if (sessionManager.isLogged) {
            String login = sessionManager.fetchUserName();
            String message = "Привіт, " + login + "!";
            textView.setText(message);
            CommonUtils.showLoading(this);
            NetworkService.getInstance()
                    .getJSONApi()
                    .profile()
                    .enqueue(new Callback<UserView>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
                            CommonUtils.hideLoading();
                            if (response.errorBody() == null && response.isSuccessful()) {
                                assert response.body() != null;
                                userProfile = response.body();
                                if (userProfile.getPhoto() == null || userProfile.getPhoto().isEmpty()) {
                                    imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/testAvatarHen.jpg");
                                } else {
                                    imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/" + userProfile.getPhoto());
                                }
                            } else {
                                userProfile = null;
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {
                            CommonUtils.hideLoading();
                            userProfile = null;
                            t.printStackTrace();
                        }
                    });
        } else {
            String message = "Привіт, гість!";
            textView.setText(message);
        }
    }
}
