package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.caloriescounter.models.Photo;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.ImageRequester;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.example.caloriescounter.network.utils.FileUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private ImageRequester imageRequester;
    private NetworkImageView editImage;
    private final String BASE_URL = NetworkService.getBaseUrl();
    private UserView userProfile;
    private SessionManager sessionManager;
    private final Calendar calendar = Calendar.getInstance();
    private TextInputEditText textEditName;
    private TextInputEditText textEditSurname;
    private TextInputEditText textEditBirthDate;
    private TextInputEditText textEditPhone;
    private TextInputEditText textEditEmail;

    private void showDatePicker(View view) {
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) ProfileActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // date picker dialog
        DatePickerDialog datePicker = new DatePickerDialog(ProfileActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int yearSelect, int monthSelect, int daySelect) {
                        calendar.set(yearSelect, monthSelect, daySelect);
                        textEditBirthDate.setText(daySelect + "/" + (monthSelect + 1) + "/" + yearSelect);
                    }
                }, year, month, day);
        datePicker.show();
    }

    private void showAlertDialogAndChangePhoto(final String photoBase64) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Are you sure you want to change photo")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String url_photo = BASE_URL + "/images/" + userProfile.getPhoto();

                        imageRequester.getImageLoader().get(url_photo, new ImageLoader.ImageListener() {
                            @Override
                            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                                Bitmap bitmap = imageContainer.getBitmap();
                                //use bitmap
                                editImage.setImageBitmap(bitmap);
                            }

                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                            }
                        });
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.showLoading(ProfileActivity.this);
                        final Photo model = new Photo();
                        model.setImageBase64(photoBase64);

                        NetworkService.getInstance()
                                .getJSONApi()
                                .updatePhoto(model)
                                .enqueue(new Callback<UserView>() {
                                    @Override
                                    public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
                                        CommonUtils.hideLoading();
                                        if (response.errorBody() == null && response.isSuccessful()) {
                                            userProfile = response.body();
                                            String succeed = "Update have been done";
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    succeed, Toast.LENGTH_LONG);
                                            toast.show();
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
                                    public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {
                                        CommonUtils.hideLoading();
                                        String error = "Error occurred while getting request!";
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                error, Toast.LENGTH_LONG);
                                        toast.show();
                                        t.printStackTrace();
                                    }
                                });
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE) {
            if (resultCode == -1) {
                Uri fileUri = data.getData();
                try {
                    File imgFile = FileUtils.from(getApplicationContext(), fileUri);
                    byte[] buffer = new byte[(int) imgFile.length() + 100];
                    int length = new FileInputStream(imgFile).read(buffer);
                    String chooseImageBase64 = Base64.encodeToString(buffer, 0, length, Base64.NO_WRAP);

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    editImage.setImageBitmap(myBitmap);

                    if (chooseImageBase64 != null && !chooseImageBase64.isEmpty()) {
                        showAlertDialogAndChangePhoto(chooseImageBase64);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClickSelectImage(View view) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("image/*");
        chooseFile = Intent.createChooser(chooseFile, "Оберіть фото");
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager = SessionManager.getInstance(ProfileActivity.this);
        imageRequester = ImageRequester.getInstance();

        editImage = findViewById(R.id.chooseImageProfile);
        ImageView changeImageBrush = findViewById(R.id.chooseImageBrush);

        textEditName = findViewById(R.id.textProfileName);
        textEditSurname = findViewById(R.id.textProfileSurname);

        textEditBirthDate = findViewById(R.id.textProfileDateOfBirth);
        textEditBirthDate.setInputType(InputType.TYPE_NULL);

        textEditPhone = findViewById(R.id.textProfilePhone);
        textEditEmail = findViewById(R.id.textProfileEmail);

        textEditBirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(view);
                }
            }
        });

        textEditBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view);
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectImage(view);
            }
        });

        changeImageBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelectImage(view);
            }
        });

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
                            textEditName.setText(userProfile.getName());
                            textEditSurname.setText(userProfile.getSurname());

                            calendar.setTime(userProfile.getDateOfBirth());
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int month = calendar.get(Calendar.MONTH);
                            int year = calendar.get(Calendar.YEAR);
                            textEditBirthDate.setText(day + "/" + (month + 1) + "/" + year);

                            textEditPhone.setText(userProfile.getPhone());
                            textEditEmail.setText((userProfile.getEmail()));
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
    }

    public void onClickUpdate(View view) {
        final UserView userUpdate = new UserView();
        userUpdate.setName(Objects.requireNonNull(textEditName.getText()).toString());
        userUpdate.setSurname(Objects.requireNonNull(textEditSurname.getText()).toString());

        userUpdate.setDateOfBirth(calendar.getTime());
        userUpdate.setPhone(Objects.requireNonNull(textEditPhone.getText()).toString());
        userUpdate.setEmail(Objects.requireNonNull(textEditEmail.getText()).toString());

        userUpdate.setPhoto(userProfile.getPhoto());
        userUpdate.setRegistrationDate(userProfile.getRegistrationDate());

        if (userUpdate.equals(userProfile)) {
            String warnings = "Nothing to update!";
            Toast toast = Toast.makeText(getApplicationContext(),
                    warnings, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .update(userUpdate)
                .enqueue(new Callback<UserView>() {
                    @Override
                    public void onResponse(@NonNull Call<UserView> call, @NonNull Response<UserView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            userProfile = response.body();
                            sessionManager.saveUserLogin(userProfile.getEmail());

                            String succeed = "Update have been done";
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    succeed, Toast.LENGTH_LONG);
                            toast.show();
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
                    public void onFailure(@NonNull Call<UserView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        String error = "Error occurred while getting request!";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }
}