package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.caloriescounter.elements.CircularProgressBar;
import com.example.caloriescounter.models.SetWaterTimeView;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.WaterSettingsView;
import com.example.caloriescounter.models.WaterTimeView;
import com.example.caloriescounter.models.WaterView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.SessionManager;
import com.example.caloriescounter.network.utils.AlarmReceiver;
import com.example.caloriescounter.network.utils.AlarmReceiverOnBoot;
import com.example.caloriescounter.network.utils.CommonUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
//import com.example.caloriescounter.network.utils.MyWorker;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterActivity extends BaseActivity implements View.OnClickListener {

    private CircularProgressBar mCountUpBar;
    private SessionManager sessionManager;
    private String requiredFieldError = "Заповніть поле";
    private final Calendar calendar = Calendar.getInstance();
    private Calendar timerCalendar = Calendar.getInstance();
    private Date currentTime;
    private Date timeBegin, timeEnd;
    private EditText txtUserWaterVolume;
    private WaterView waterView;
    private WaterTimeView waterSettings;
    private DateFormat timeFormat;

    private Button btnAdd100, btnAdd200, btnAdd300, btnAdd400, btnAdd500, btnAdd600;
    private Button btnSetEnd, btnSetBegin, btnSetVolume;

    int myHour;
    int myMinute;
    SimpleDateFormat formatDate = new SimpleDateFormat("MMM d, yyyy", new Locale("uk", "UA"));
    public TextView txtDate;
    NotificationManager notificationManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_water);
        this.getSupportActionBar().setTitle("Лічильник води");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sessionManager = SessionManager.getInstance(this);
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        currentTime = Calendar.getInstance().getTime();
        txtDate = findViewById(R.id.dateNow);
        txtUserWaterVolume = findViewById(R.id.txtUserWaterVolume);

        btnAdd100 = findViewById(R.id.btnAdd100);
        btnAdd200 = findViewById(R.id.btnAdd200);
        btnAdd300 = findViewById(R.id.btnAdd300);
        btnAdd400 = findViewById(R.id.btnAdd400);
        btnAdd500 = findViewById(R.id.btnAdd500);
        btnAdd600 = findViewById(R.id.btnAdd600);
        btnSetEnd = findViewById(R.id.btnSetEnd);
        btnSetBegin = findViewById(R.id.btnSetBegin);
        btnSetVolume = findViewById(R.id.btnSetVolume);
        btnAdd100.setOnClickListener(this);
        btnAdd200.setOnClickListener(this);
        btnAdd300.setOnClickListener(this);
        btnAdd400.setOnClickListener(this);
        btnAdd500.setOnClickListener(this);
        btnAdd600.setOnClickListener(this);

        this.mCountUpBar = (CircularProgressBar) this.findViewById(R.id.countup_bar2);
        txtDate.setText(formatDate.format(currentTime));
        Locale locale = new Locale("uk", "UA");
        Locale.setDefault(locale);

        if (!sessionManager.isLogged) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(v);
            }
        });
        btnSetVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDailyWater(v);
            }
        });

        txtUserWaterVolume.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    setDailyWater(v);
                    return true;
                }
                return false;
            }
        });

        NetworkService.getInstance()
                .getJSONApi()
                .getWaterSettings()
                .enqueue(new Callback<WaterTimeView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WaterTimeView> call, @NonNull Response<WaterTimeView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            waterSettings = response.body();
                            timerCalendar.set(Calendar.HOUR_OF_DAY, waterSettings.getBeginHour());
                            timerCalendar.set(Calendar.MINUTE, waterSettings.getBeginMinute());
                            timeBegin = timerCalendar.getTime();
                            btnSetBegin.setText(timeFormat.format(timeBegin));
                            timerCalendar.set(Calendar.HOUR_OF_DAY, waterSettings.getEndHour());
                            timerCalendar.set(Calendar.MINUTE, waterSettings.getEndMinute());
                            timeEnd = timerCalendar.getTime();
                            btnSetEnd.setText(timeFormat.format(timeEnd));
                            txtUserWaterVolume.setText(Integer.toString(waterSettings.getDailyVolume()));
                            mCountUpBar.setMax(waterSettings.getDailyVolume());
                        } else {
                            waterSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterTimeView> call, @NonNull Throwable t) {
                        waterSettings = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
        GetWater();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    public void DrinkWater(WaterView model) {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .addWater(model)
                .enqueue(new Callback<WaterView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WaterView> call, @NonNull Response<WaterView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            waterView = response.body();
                            WaterActivity.this.mCountUpBar.setProgress(waterView.getWaterVolume());
                        } else {
                            waterView = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterView> call, @NonNull Throwable t) {

                        waterView = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void SetTimer() {
        Intent intent = new Intent(this, AlarmReceiverOnBoot.class);
        intent.setAction("NOTIFICATION_INTENT_ACTION_WATER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);

        int beginHour = waterSettings.getBeginHour();
        int endHour = waterSettings.getEndHour();
        int beginMinute = waterSettings.getBeginMinute();
        int waterVolume = waterSettings.getDailyVolume();
        int currentWatervolume = waterView.getWaterVolume();
        Log.d("Bootbegin", Integer.toString(waterSettings.getBeginHour()));
        Log.d("Bootend", Integer.toString(waterSettings.getEndHour()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar time = Calendar.getInstance();
        int hour = time.get(Calendar.HOUR_OF_DAY);
        if (hour < beginHour) {
            time.set(Calendar.HOUR_OF_DAY, beginHour);
            time.set(Calendar.MINUTE, beginMinute);
            time.set(Calendar.SECOND, 0);

        } else if (hour > endHour || currentWatervolume >= waterVolume) {
            time.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            time.set(Calendar.HOUR_OF_DAY, beginHour);
            time.set(Calendar.MINUTE, beginMinute);
            time.set(Calendar.SECOND, 0);

        } else if (hour + 1 > endHour || currentWatervolume >= waterVolume) {
            time.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
            time.set(Calendar.HOUR_OF_DAY, beginHour);
            time.set(Calendar.MINUTE, beginMinute);
            time.set(Calendar.SECOND, 0);
        } else {
            time.setTimeInMillis(System.currentTimeMillis());
            time.add(Calendar.HOUR, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //  alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
                    AlarmManager.INTERVAL_HOUR, pendingIntent);
        }
        String message = "Наступне нагадування о ";
        Toast toast = Toast.makeText(getApplicationContext(),
                message + time.getTime().toString(), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        final WaterView model = new WaterView();
        model.setDateOfDrink(currentTime);

        switch (v.getId()) {
            case R.id.btnAdd100:
                model.setWaterVolume(100);
                break;
            case R.id.btnAdd200:
                model.setWaterVolume(200);
                break;
            case R.id.btnAdd300:
                model.setWaterVolume(300);
                break;
            case R.id.btnAdd400:
                model.setWaterVolume(400);
                break;
            case R.id.btnAdd500:
                model.setWaterVolume(500);
                break;
            case R.id.btnAdd600:
                model.setWaterVolume(600);
                break;
        }
        DrinkWater(model);
        SetTimer();
    }

    public void onClickSetBegin(View view) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WaterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myHour = hourOfDay;
                myMinute = minute;
                SetWaterTimeView model = new SetWaterTimeView();
                model.setHour(myHour);
                model.setMinute(myMinute);
                CommonUtils.showLoading(WaterActivity.this);
                NetworkService.getInstance()
                        .getJSONApi()
                        .setWaterBegin(model)
                        .enqueue(new Callback<WaterTimeView>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(@NonNull Call<WaterTimeView> call, @NonNull Response<WaterTimeView> response) {
                                CommonUtils.hideLoading();
                                if (response.errorBody() == null && response.isSuccessful()) {
                                    assert response.body() != null;
                                    waterSettings = response.body();
                                    timerCalendar.set(Calendar.HOUR_OF_DAY, waterSettings.getBeginHour());
                                    timerCalendar.set(Calendar.MINUTE, waterSettings.getBeginMinute());
                                    timeBegin = timerCalendar.getTime();
                                    btnSetBegin.setText(timeFormat.format(timeBegin));
                                } else {
                                    waterSettings = null;
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<WaterTimeView> call, @NonNull Throwable t) {
                                waterSettings = null;
                                CommonUtils.hideLoading();
                                String error = "Помилка з'єднання";
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        error, Toast.LENGTH_LONG);
                                toast.show();
                                t.printStackTrace();
                            }
                        });
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    public void onClickSetEnd(View view) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WaterActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myHour = hourOfDay;
                myMinute = minute;
                SetWaterTimeView model = new SetWaterTimeView();
                model.setHour(myHour);
                model.setMinute(myMinute);
                if (myHour < waterSettings.getBeginHour()) {
                    new MaterialAlertDialogBuilder(WaterActivity.this)
                            .setTitle("Попередження")
                            .setMessage("Перевірте правильність обраного часу, сповіщення відбуватимуться у нічний час.")
                            .setPositiveButton("OK", null)
                            .show();
                }
                CommonUtils.showLoading(WaterActivity.this);
                NetworkService.getInstance()
                        .getJSONApi()
                        .setWaterEnd(model)
                        .enqueue(new Callback<WaterTimeView>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(@NonNull Call<WaterTimeView> call, @NonNull Response<WaterTimeView> response) {
                                CommonUtils.hideLoading();
                                if (response.errorBody() == null && response.isSuccessful()) {
                                    assert response.body() != null;
                                    waterSettings = response.body();
                                    timerCalendar.set(Calendar.HOUR_OF_DAY, waterSettings.getEndHour());
                                    timerCalendar.set(Calendar.MINUTE, waterSettings.getEndMinute());
                                    timeEnd = timerCalendar.getTime();
                                    btnSetEnd.setText(timeFormat.format(timeEnd));
                                } else {
                                    waterSettings = null;
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<WaterTimeView> call, @NonNull Throwable t) {

                                waterSettings = null;
                                CommonUtils.hideLoading();
                                String error = "Помилка з'єднання";
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        error, Toast.LENGTH_LONG);
                                toast.show();
                                t.printStackTrace();
                            }
                        });
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showDatePicker(View view) {
        //hide keyboard
        InputMethodManager imm = (InputMethodManager) WaterActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(WaterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int yearSelect, int monthSelect, int daySelect) {
                        calendar.set(yearSelect, monthSelect, daySelect);
                        Date currentTime = calendar.getTime();
                        txtDate.setText(formatDate.format(currentTime));
                        GetWater();
                    }
                }, year, month, day);
        datePicker.show();
        GetWater();
    }

    public void onClickPreviousDate(View view) {
        calendar.add(Calendar.DATE, -1);
        Date currentTime = calendar.getTime();
        txtDate.setText(formatDate.format(currentTime));
        GetWater();
    }

    public void onClickNextDate(View view) {
        calendar.add(Calendar.DATE, 1);
        Date currentTime = calendar.getTime();
        txtDate.setText(formatDate.format(currentTime));
        GetWater();
    }

    public void GetWater() {
        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .getWater(calendar.getTime())
                .enqueue(new Callback<WaterView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WaterView> call, @NonNull Response<WaterView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            waterView = response.body();
                            WaterActivity.this.mCountUpBar.setProgress(waterView.getWaterVolume());
                        } else {
                            waterView = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterView> call, @NonNull Throwable t) {
                        waterView = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void setDailyWater(View v) {
        final TextInputLayout userWaterVolumeLayout = findViewById(R.id.userWaterVolumeLayout);
        boolean isCorrect = true;
        if (Objects.requireNonNull(txtUserWaterVolume.getText()).toString().equals("")) {
            userWaterVolumeLayout.setError(requiredFieldError);
            isCorrect = false;
        } else {
            userWaterVolumeLayout.setError(null);
        }
        if (!isCorrect)
            return;

        int volume = Integer.parseInt(txtUserWaterVolume.getText().toString());

        CommonUtils.showLoading(this);
        NetworkService.getInstance()
                .getJSONApi()
                .setdailyvolume(volume)
                .enqueue(new Callback<WaterTimeView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WaterTimeView> call, @NonNull Response<WaterTimeView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            waterSettings = response.body();
                            txtUserWaterVolume.setText(Integer.toString(waterSettings.getDailyVolume()));
                            txtUserWaterVolume.clearFocus();
                        } else {
                            waterSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterTimeView> call, @NonNull Throwable t) {

                        waterSettings = null;
                        CommonUtils.hideLoading();
                        String error = "Помилка з'єднання";
                        Toast toast = Toast.makeText(getApplicationContext(),
                                error, Toast.LENGTH_LONG);
                        toast.show();
                        t.printStackTrace();
                    }
                });
    }

    public void OnClickTimer(View view) {
        Intent intent = new Intent(this, AlarmReceiverOnBoot.class);
        intent.setAction("NOTIFICATION_INTENT_ACTION_WATER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar time = Calendar.getInstance();
        int i = 10;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + (i * 1000), pendingIntent);
            String message = "Наступне нагадування о ";
            Toast toast = Toast.makeText(getApplicationContext(),
                    message + time.getTime().toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}


