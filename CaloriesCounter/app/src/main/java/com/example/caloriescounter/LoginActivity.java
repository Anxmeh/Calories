package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.caloriescounter.models.LoginGoogleView;
import com.example.caloriescounter.models.LoginView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.Tokens;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private UserView userProfile;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_login);
        this.getSupportActionBar().setTitle("Вхід");
        sessionManager = SessionManager.getInstance(LoginActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("951922290898-kgfog8u4i0q0qo8ms93817n7aejv746c.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInBtn = findViewById(R.id.signInGoogle);
        signInBtn.setOnClickListener(new View.OnClickListener() {

            private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }

            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInGoogle:
                        signIn();
                        break;
                }
            }
        });
    }

    public void onClickSignIn(View view) {
        final TextInputEditText password = findViewById(R.id.inputPassword);
        final TextInputEditText email = findViewById(R.id.inputEmail);

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
                            sessionManager.saveJWTToken(token.getToken());
                            sessionManager.saveUserLogin(model.getEmail());

                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .profile()
                                    .enqueue(new Callback<UserView>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
                                            if (response.errorBody() == null && response.isSuccessful()) {
                                                assert response.body() != null;
                                                userProfile = response.body();
                                                sessionManager.saveUserName(userProfile.getName());
                                            } else {
                                                userProfile = null;
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {
                                            userProfile = null;
                                            CommonUtils.hideLoading();
                                            String error = "Помилка з'єднання!";
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    error, Toast.LENGTH_LONG);
                                            toast.show();
                                            t.printStackTrace();
                                        }
                                    });

                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
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

    @Override
    public void onStart() {
        super.onStart();
        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            findViewById(R.id.signInGoogle).setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.w(TAG, "idToken" + idToken);
            LoginGoogleView model = new LoginGoogleView(idToken);

            CommonUtils.showLoading(this);
            NetworkService.getInstance()
                    .getJSONApi()
                    .loginGoogle(model)
                    .enqueue(new Callback<Tokens>() {
                        @Override
                        public void onResponse(@NonNull Call<Tokens> call, @NonNull Response<Tokens> response) {
                            CommonUtils.hideLoading();
                            if (response.errorBody() == null && response.isSuccessful()) {
                                Tokens token = response.body();
                                assert token != null;
                                sessionManager.saveJWTToken(token.getToken());
                                sessionManager.saveUserLogin(account.getEmail());

                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .profile()
                                        .enqueue(new Callback<UserView>() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
                                                if (response.errorBody() == null && response.isSuccessful()) {
                                                    assert response.body() != null;
                                                    userProfile = response.body();
                                                    sessionManager.saveUserName(userProfile.getName());
                                                } else {
                                                    userProfile = null;
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {

                                                userProfile = null;
                                                CommonUtils.hideLoading();
                                                String error = "Помилка з'єднання!";
                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                        error, Toast.LENGTH_LONG);
                                                toast.show();
                                                t.printStackTrace();
                                            }
                                        });

                                Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
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
                            String error = "Помилка з'єднання!";
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    error, Toast.LENGTH_LONG);
                            toast.show();
                            t.printStackTrace();
                        }
                    });

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            updateUI(null);
        }
    }
}