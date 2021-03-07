package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.models.LoginGoogleView;
import com.example.caloriescounter.models.RegisterView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.Tokens;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.FileUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    public static final int PICKFILE_RESULT_CODE = 1;
    private ImageRequester imageRequester;
    private NetworkImageView editImage;
    private String chooseImageBase64;
    private final String BASE_URL = NetworkService.getBaseUrl();
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SessionManager sessionManager;
    private UserView userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_register);
        this.getSupportActionBar().setTitle("Реєстрація");

        imageRequester = ImageRequester.getInstance();
        editImage = findViewById(R.id.chooseImageRegister);
        imageRequester.setImageFromUrl(editImage, BASE_URL + "/images/testAvatarHen.jpg");
        sessionManager = SessionManager.getInstance(RegisterActivity.this);

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

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
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

                                Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
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
            updateUI(null);
        }
    }

    public void onClickRegister(View view) {
        final TextInputEditText email = findViewById(R.id.inputEmailRegister);
        final TextInputLayout emailLayout = findViewById(R.id.emailLayoutRegister);

        final TextInputEditText password = findViewById(R.id.inputPasswordRegister);
        final TextInputLayout passwordLayout = findViewById(R.id.passwordLayoutRegister);

        final TextInputEditText passwordConfirm = findViewById(R.id.inputPasswordConfirmRegister);
        final TextInputLayout passwordConfirmLayout = findViewById(R.id.passwordConfirmLayoutRegister);

        boolean isCorrect = true;

        if (Objects.requireNonNull(email.getText()).toString().equals("")) {
            emailLayout.setError("Required field!");
            isCorrect = false;
        } else {
            emailLayout.setError(null);
        }

        if (Objects.requireNonNull(password.getText()).toString().equals("")) {
            passwordLayout.setError("Required field!");
            isCorrect = false;
        } else {
            passwordLayout.setError(null);
        }

        if (Objects.requireNonNull(passwordConfirm.getText()).toString().equals("")) {
            passwordConfirmLayout.setError("Required field!");
            isCorrect = false;
        } else if (!password.getText().toString().equals(passwordConfirm.getText().toString())) {
            passwordConfirmLayout.setError("Confirm your password!");
            isCorrect = false;
        } else {
            passwordConfirmLayout.setError(null);
        }

        if (!isCorrect)
            return;

        CommonUtils.showLoading(this);
        final RegisterView model = new RegisterView();
        model.setEmail(email.getText().toString());
        model.setPassword(password.getText().toString());
        model.setImageBase64(chooseImageBase64);

        NetworkService.getInstance()
                .getJSONApi()
                .register(model)
                .enqueue(new Callback<Tokens>() {
                    @Override
                    public void onResponse(@NonNull Call<Tokens> call, @NonNull Response<Tokens> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            Tokens token = response.body();
                            assert token != null;

                            SessionManager sessionManager = SessionManager.getInstance(RegisterActivity.this);
                            sessionManager.saveJWTToken(token.getToken());
                            sessionManager.saveUserLogin(model.getEmail());

                            Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE: {
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    try {
                        File imgFile = FileUtils.from(getApplicationContext(), fileUri);
                        byte[] buffer = new byte[(int) imgFile.length() + 100];
                        int length = new FileInputStream(imgFile).read(buffer);
                        chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        editImage.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case RC_SIGN_IN: {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                break;
            }

        }
    }

    public void onClickSelectImage(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("image/*");
        chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }
}