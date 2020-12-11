package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.caloriescounter.data.UserRepository;
import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.models.LoginView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.Tokens;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ImageRequester imageRequester;
    private NetworkImageView editImage;
    private final String BASE_URL = NetworkService.getBaseUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        imageRequester = ImageRequester.getInstance();
//        editImage = findViewById(R.id.chooseImageLogin);
//        imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/testAvatarHen.jpg");
    }

    public void onClickSignIn(View view) {
        final TextInputEditText password = findViewById(R.id.input_password);
        final TextInputEditText email = findViewById(R.id.input_email);

        CommonUtils.showLoading(this);
        final LoginView model = new LoginView();
        model.setEmail(Objects.requireNonNull(email.getText()).toString());
        model.setPassword(Objects.requireNonNull(password.getText()).toString());
        NetworkService.getInstance()
                .getJSONApi()
                .login(model)
                .enqueue(new Callback<Tokens>() {
                    @Override
                    public void onResponse(@NonNull Call<Tokens> call, @NonNull Response<Tokens> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            Tokens token = response.body();
                            assert token != null;

                            SessionManager sessionManager = SessionManager.getInstance(LoginActivity.this);

                            sessionManager.saveJWTToken(token.getToken());
                            sessionManager.saveUserLogin(model.getEmail());

                            Intent intent = new Intent(LoginActivity.this, ProductsActivity.class);
                            startActivity(intent);
                        } else {
                            String errorMessage;
                            try {
                                assert response.errorBody() != null;
                                errorMessage = response.errorBody().string();
                            } catch (IOException e) {
                                errorMessage = response.message();
                                e.printStackTrace();
                            }
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    errorMessage, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Tokens> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Error occurred while getting request!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void onClickSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);

    }


}