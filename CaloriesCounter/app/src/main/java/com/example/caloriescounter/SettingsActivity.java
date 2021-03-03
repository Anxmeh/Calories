package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.UserView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends BaseActivity {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout drawerLayout;
    private SessionManager sessionManager;
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
    private String requiredFieldError = "Заповніть поле";

    private UserSettingsView userSettings;
    boolean sex;
    double activityIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_settings);
        this.getSupportActionBar().setTitle("Мої параметри");
//        Toolbar homeToolbar = findViewById(R.id.home_toolbar);
//        homeToolbar.setTitle("Параметри");
//        setSupportActionBar(homeToolbar);
//
//        drawerLayout = findViewById(R.id.drawerLayout);
//        NavigationView navigationView = findViewById(R.id.navigation);
//        navigationView.bringToFront();
//        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(mToggle);
//        mToggle.syncState();
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                return onNavItemSelected(item);
//            }
//        });
//
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        sessionManager = SessionManager.getInstance(this);

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

                            switch (String.valueOf(userSettings.getActivity())) {
                                case "1.2":
                                radioGroupActivity.check(R.id.radioMinimum);
                                break;
                                case "1.375":
                                    radioGroupActivity.check(R.id.radioLow);
                                    break;
                                case "1.55":
                                    radioGroupActivity.check(R.id.radioMedium);
                                    break;
                                case "1.725":
                                    radioGroupActivity.check(R.id.radioHigh);
                                    break;
                                case "1.9":
                                    radioGroupActivity.check(R.id.radioVeryHigh);
                                    break;
                            }



                        } else {
                            userSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserSettingsView> call, @NonNull Throwable t) {
                                               userSettings = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });


    }

    public void onClickSave(View view) {
        final UserSettingsView settingsUpdate = new UserSettingsView();
        boolean isCorrect = true;



       // final TextInputEditText email = findViewById(R.id.input_emailRegister);

        final TextInputLayout heightlLayout = findViewById(R.id.heightLayout);
        final TextInputLayout weightLayout = findViewById(R.id.weightLayout);
        final TextInputLayout chestLayout = findViewById(R.id.chestLayout);
        final TextInputLayout waistLayout = findViewById(R.id.waistLayout);
        final TextInputLayout hipsLayout = findViewById(R.id.hipsLayout);
        final TextInputLayout hipLayout = findViewById(R.id.hipLayout);
        final TextInputLayout wristLayout = findViewById(R.id.wristLayout);
       // final TextInputLayout ageLayout = findViewById(R.id.ageLayout); !!!!!!!
        final TextInputLayout shinLayout = findViewById(R.id.shinLayout);
        final TextInputLayout forearmLayout = findViewById(R.id.forearmLayout);
        final TextInputLayout neckLayout = findViewById(R.id.neckLayout);
        final TextInputLayout userCaloriesLayout = findViewById(R.id.userCaloriesLayout);

        if (Objects.requireNonNull(txtHeight.getText()).toString().equals("")) {
            heightlLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            heightlLayout.setError(null);
        }
        if (Objects.requireNonNull(txtWeight.getText()).toString().equals("")) {
            weightLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            weightLayout.setError(null);
        }
        if (Objects.requireNonNull(txtChest.getText()).toString().equals("")) {
            chestLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            chestLayout.setError(null);
        }
        if (Objects.requireNonNull(txtWaist.getText()).toString().equals("")) {
            waistLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            waistLayout.setError(null);
        }
        if (Objects.requireNonNull(txtHips.getText()).toString().equals("")) {
            hipsLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            hipsLayout.setError(null);
        }
        if (Objects.requireNonNull(txtHip.getText()).toString().equals("")) {
            hipLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            hipLayout.setError(null);
        }
        if (Objects.requireNonNull(txtWrist.getText()).toString().equals("")) {
            wristLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            wristLayout.setError(null);
        }
        if (Objects.requireNonNull(txtShin.getText()).toString().equals("")) {
            shinLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            shinLayout.setError(null);
        }
        if (Objects.requireNonNull(txtForearm.getText()).toString().equals("")) {
            forearmLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            forearmLayout.setError(null);
        }
        if (Objects.requireNonNull(txtNeck.getText()).toString().equals("")) {
            neckLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            neckLayout.setError(null);
        }

        if (Objects.requireNonNull(txtUserCalories.getText()).toString().equals("")) {
            userCaloriesLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            userCaloriesLayout.setError(null);
        }

        if (!isCorrect)
            return;

 settingsUpdate.setWeight(Double.parseDouble(txtWeight.getText().toString()));
         settingsUpdate.setHeight(Double.parseDouble(txtHeight.getText().toString()));
        settingsUpdate.setChest(Double.parseDouble(txtChest.getText().toString()));
        settingsUpdate.setWaist(Double.parseDouble(txtWaist.getText().toString()));
        settingsUpdate.setHips(Double.parseDouble(txtHips.getText().toString()));
        settingsUpdate.setHip(Double.parseDouble(txtHip.getText().toString()));
        settingsUpdate.setWrist(Double.parseDouble(txtWrist.getText().toString()));
        settingsUpdate.setAge(30);
        settingsUpdate.setShin(Double.parseDouble(txtShin.getText().toString()));
        settingsUpdate.setForearm(Double.parseDouble(txtForearm.getText().toString()));
        settingsUpdate.setNeck(Double.parseDouble(txtNeck.getText().toString()));
        settingsUpdate.setUserCalories(Double.parseDouble(txtUserCalories.getText().toString()));

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

                        userSettings = null;

                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
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

                        userSettings = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });




    }
//    @SuppressLint("NonConstantResourceId")
//    public boolean onNavItemSelected(MenuItem menuItem) {
//        Intent intent;
//        Toast toast;
//        // Handle item selection
//        switch (menuItem.getItemId()) {
//            case R.id.main:
//                drawerLayout.closeDrawers();
//                break;
//            case R.id.products:
//                intent = new Intent(this, ProductsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.newDish:
//                intent = new Intent(this, RecyclerActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.newProduct:
//                intent = new Intent(this, AddProductActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.dailyMenu:
//                intent = new Intent(this, TodayActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.userSettings:
//                intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.login:
//                intent = new Intent(this, LoginActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.register:
//                intent = new Intent(this, RegisterActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.profile:
//                if (!sessionManager.isLogged) {
//                    intent = new Intent(this, LoginActivity.class);
//                } else {
//                    intent = new Intent(this, ProfileActivity.class);
//                }
//                startActivity(intent);
//                break;
//            case R.id.logout:
//                sessionManager = SessionManager.getInstance(this);
//                String message = "See you later!";
//                sessionManager.logout();
//                toast = Toast.makeText(getApplicationContext(),
//                        "You have been signed out successfully", Toast.LENGTH_LONG);
//                toast.show();
//                drawerLayout.closeDrawers();
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}