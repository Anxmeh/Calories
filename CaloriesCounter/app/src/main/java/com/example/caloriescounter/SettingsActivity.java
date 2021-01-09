package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.CommonUtils;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private EditText txtHeight;
    private EditText txtHeightCm;
    private EditText txtWeight;
    private EditText txtChest;
    private EditText txtWaist;
    private EditText txtHips;
    private EditText txtHip;
    private EditText txtWrist;
    private EditText txtShin;
    private EditText txtNeck;
    private EditText txtForearm;
    private TextView txtCalories;
    private TextView txtFatPercent;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private EditText txtUserCalories;
    private RadioGroup radioGroupActivity;
    private RadioGroup radioGroupSex;

    private UserSettingsView userSettings;
    boolean sex;
    double activityIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtCalories = findViewById(R.id.txtCalories);
        txtHeight = findViewById(R.id.txtHeight);
//        txtHeightCm = findViewById(R.id.txtHeightCm);
        txtWeight = findViewById(R.id.txtWeight);
        txtChest = findViewById(R.id.txtChest);
        txtWaist = findViewById(R.id.txtWaist);
        txtWrist = findViewById(R.id.txtWrist);
        txtHips = findViewById(R.id.txtHips);
        txtHip = findViewById(R.id.txtHip);
        txtShin = findViewById(R.id.txtShin);
        txtNeck = findViewById(R.id.txtNeck);
        txtForearm = findViewById(R.id.txtForearm);
        txtFatPercent = findViewById(R.id.txtFatPercent);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);
        txtUserCalories = findViewById(R.id.txtUserCalories);


        radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioMale:
                        sex = true;
                        break;
                    case R.id.radioFemale:
                        sex = false;
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupActivity = (RadioGroup) findViewById(R.id.radioGroupActivity);
        radioGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioMinimum:
                        activityIndex = 1.2;
                        break;
                    case R.id.radioLow:
                        activityIndex = 1.375;
                        break;
                    case R.id.radioMedium:
                        activityIndex = 1.55;
                        break;
                    case R.id.radioHigh:
                        activityIndex = 1.725;
                        break;
                    case R.id.radioVeryHigh:
                        activityIndex = 1.9;
                        break;
                    default:
                        break;
                }
            }
        });


        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .settings()
                .enqueue(new Callback<UserSettingsView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<UserSettingsView> call, @NonNull Response<UserSettingsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userSettings = response.body();
                          //  double heigh = userSettings.getHeight();
                          // int heightM = (int) userSettings.getHeight() / 100;
                          //  int heightCm = (int) userSettings.getHeight() - (100 * heightM);

                            txtHeight.setText(Double.toString(userSettings.getHeight()));
                           // txtHeightCm.setText(Integer.toString(heightCm));

                            txtWeight.setText(Double.toString(userSettings.getWeight()));
                            txtChest.setText(Double.toString(userSettings.getChest()));
                            txtWaist.setText(Double.toString(userSettings.getWaist()));
                            txtHips.setText(Double.toString(userSettings.getHips()));
                            txtHip.setText(Double.toString(userSettings.getHip()));
                            txtWrist.setText(Double.toString(userSettings.getWrist()));
                            txtShin.setText(Double.toString(userSettings.getShin()));
                            txtNeck.setText(Double.toString(userSettings.getNeck()));
                            txtForearm.setText(Double.toString(userSettings.getForearm()));
                            txtCalories.setText(Double.toString(userSettings.getCalories()));
                            txtFatPercent.setText(Double.toString(userSettings.getFatPercentage()));
                            txtUserCalories.setText(Double.toString(userSettings.getUserCalories()));
//                            if (userSettings.isSex()==true) {
//                                radioMale.setChecked(true);
//                            }
//                            else {
//                                radioFemale.setChecked(true);
//                            }

                            // userSettings.isSex() ? radioMale.setChecked(true) : radioFemale.setChecked(true);
                            //  radioMale.setChecked( userSettings.isSex());
                            if (userSettings.isSex())
                                radioGroupSex.check(R.id.radioMale);
                            else
                                radioGroupSex.check(R.id.radioFemale);


                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        userSettings = null;
                        t.printStackTrace();
                    }
                });


    }

    public void onClickSave(View view) {
        final UserSettingsView settingsUpdate = new UserSettingsView();
        settingsUpdate.setWeight(Objects.requireNonNull(Double.parseDouble(txtWeight.getText().toString())));
         settingsUpdate.setHeight(Objects.requireNonNull(Double.parseDouble(txtHeight.getText().toString())));
        settingsUpdate.setChest(Objects.requireNonNull(Double.parseDouble(txtChest.getText().toString())));
        settingsUpdate.setWaist(Objects.requireNonNull(Double.parseDouble(txtWaist.getText().toString())));
        settingsUpdate.setHips(Objects.requireNonNull(Double.parseDouble(txtHips.getText().toString())));
        settingsUpdate.setHip(Objects.requireNonNull(Double.parseDouble(txtHip.getText().toString())));
        settingsUpdate.setWrist(Objects.requireNonNull(Double.parseDouble(txtWrist.getText().toString())));
        settingsUpdate.setAge(30);
        settingsUpdate.setHeight(160);
        settingsUpdate.setShin(Objects.requireNonNull(Double.parseDouble(txtShin.getText().toString())));
        settingsUpdate.setForearm(Objects.requireNonNull(Double.parseDouble(txtForearm.getText().toString())));
        settingsUpdate.setNeck(Objects.requireNonNull(Double.parseDouble(txtNeck.getText().toString())));
        settingsUpdate.setUserCalories(Objects.requireNonNull(Double.parseDouble(txtUserCalories.getText().toString())));


        RadioGroup radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioMale:
                        sex = true;
                        break;
                    case R.id.radioFemale:
                        sex = false;
                        break;
                    default:
                        break;
                }
            }
        });

        RadioGroup radioGroupActivity = (RadioGroup) findViewById(R.id.radioGroupActivity);
        radioGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radioMinimum:
                        activityIndex = 1.2;
                        break;
                    case R.id.radioLow:
                        activityIndex = 1.375;
                        break;
                    case R.id.radioMedium:
                        activityIndex = 1.55;
                        break;
                    case R.id.radioHigh:
                        activityIndex = 1.725;
                        break;
                    case R.id.radioVeryHigh:
                        activityIndex = 1.9;
                        break;
                    default:
                        break;
                }
            }
        });
        settingsUpdate.setSex(sex);
        settingsUpdate.setActivity(activityIndex);
        settingsUpdate.setCalories(0);
        settingsUpdate.setBmi(0);
        settingsUpdate.setFatPercentage(0);


        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .updateSettings(settingsUpdate)
                .enqueue(new Callback<UserSettingsView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<UserSettingsView> call, @NonNull Response<UserSettingsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userSettings = response.body();
//                            double heigh = userSettings.getHeight();
//                            int heightM = (int) userSettings.getHeight() / 100;
//                            int heightCm = (int) userSettings.getHeight() - (100 * heightM);
//
//                            txtHeightM.setText(Integer.toString(heightM));
//                            txtHeightCm.setText(Integer.toString(heightCm));
//
//                            txtWeight.setText(Double.toString(userSettings.getWeight()));
//                            txtChest.setText(Double.toString(userSettings.getChest()));
//                            txtWaist.setText(Double.toString(userSettings.getWaist()));
//                            txtHips.setText(Double.toString(userSettings.getHips()));
//                            txtHip.setText(Double.toString(userSettings.getHip()));
//                            txtWrist.setText(Double.toString(userSettings.getWrist()));
//                            txtCalories.setText(Double.toString(userSettings.getCalories()));
                            txtCalories.setText(Double.toString(userSettings.getCalories()));
                            txtFatPercent.setText(Double.toString(userSettings.getFatPercentage()));

                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        userSettings = null;
                        t.printStackTrace();
                    }
                });


    }

    public void onClickSaveCalories(View view) {

        double userCalories = Double.parseDouble(txtUserCalories.getText().toString());
        userSettings.setUserCalories(Double.parseDouble(txtUserCalories.getText().toString()));

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .updateCalories(userCalories)
                .enqueue(new Callback<UserSettingsView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<UserSettingsView> call, @NonNull Response<UserSettingsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            userSettings = response.body();
                            //  double heigh = userSettings.getHeight();
                            // int heightM = (int) userSettings.getHeight() / 100;
                            //  int heightCm = (int) userSettings.getHeight() - (100 * heightM);

                            txtHeight.setText(Double.toString(userSettings.getHeight()));
                            // txtHeightCm.setText(Integer.toString(heightCm));

                            txtWeight.setText(Double.toString(userSettings.getWeight()));
                            txtChest.setText(Double.toString(userSettings.getChest()));
                            txtWaist.setText(Double.toString(userSettings.getWaist()));
                            txtHips.setText(Double.toString(userSettings.getHips()));
                            txtHip.setText(Double.toString(userSettings.getHip()));
                            txtWrist.setText(Double.toString(userSettings.getWrist()));
                            txtShin.setText(Double.toString(userSettings.getShin()));
                            txtNeck.setText(Double.toString(userSettings.getNeck()));
                            txtForearm.setText(Double.toString(userSettings.getForearm()));
                            txtCalories.setText(Double.toString(userSettings.getCalories()));
                            txtFatPercent.setText(Double.toString(userSettings.getFatPercentage()));
                            txtUserCalories.setText(Double.toString(userSettings.getUserCalories()));
//                            if (userSettings.isSex()==true) {
//                                radioMale.setChecked(true);
//                            }
//                            else {
//                                radioFemale.setChecked(true);
//                            }

                            // userSettings.isSex() ? radioMale.setChecked(true) : radioFemale.setChecked(true);
                            //  radioMale.setChecked( userSettings.isSex());
                            if (userSettings.isSex())
                                radioGroupSex.check(R.id.radioMale);
                            else
                                radioGroupSex.check(R.id.radioFemale);


                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        userSettings = null;
                        t.printStackTrace();
                    }
                });




    }
}