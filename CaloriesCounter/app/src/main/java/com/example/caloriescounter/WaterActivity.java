package com.example.caloriescounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriescounter.elements.CircularProgressBar;
import com.example.caloriescounter.models.UserSettingsView;
import com.example.caloriescounter.models.WaterSettingsView;
import com.example.caloriescounter.models.WaterView;
import com.example.caloriescounter.network.NetworkService;
import com.example.caloriescounter.network.utils.AlarmReceiver;
import com.example.caloriescounter.network.utils.AlarmReceiverOnBoot;
import com.example.caloriescounter.network.utils.CommonUtils;
//import com.example.caloriescounter.network.utils.MyWorker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = WaterActivity.class.getSimpleName();
    private static final int ONE_SECOND_IN_MS = 1000;
    private CircularProgressBar mCountUpBar2;


    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private Date currentTime;
    private WaterView waterView;
    private WaterSettingsView waterSettings;

    private Button btnAdd100, btnAdd200, btnAdd300, btnAdd400, btnAdd500, btnAdd600, btnAddX;

    int wat;
    int water2;

    NotificationManager notificationManager;
    //private static final String CHANNEL_ID ="com.chikeandroid.tutsplustalerts.ANDROID" ;
    // String CHANNEL_ID = "my_channel_01";

    //String CHANNEL_ID;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.addContentView(R.layout.activity_water);
        this.getSupportActionBar().setTitle("Лічильник води");
        //  setContentView(R.layout.activity_water);
        currentTime = Calendar.getInstance().getTime();

        btnAdd100 = findViewById(R.id.btnAdd100);
        btnAdd200 = findViewById(R.id.btnAdd200);
        btnAdd300 = findViewById(R.id.btnAdd300);
        btnAdd400 = findViewById(R.id.btnAdd400);
        btnAdd500 = findViewById(R.id.btnAdd500);
        btnAdd600 = findViewById(R.id.btnAdd600);
        btnAddX = findViewById(R.id.btnAddX);
        btnAdd100.setOnClickListener(this);
        btnAdd200.setOnClickListener(this);
        btnAdd300.setOnClickListener(this);
        btnAdd400.setOnClickListener(this);
        btnAdd500.setOnClickListener(this);
        btnAdd600.setOnClickListener(this);
        btnAddX.setOnClickListener(this);


        calendar2.set(Calendar.HOUR_OF_DAY, 15);
        calendar2.set(Calendar.MINUTE, 10);
        calendar2.set(Calendar.SECOND, 0);


        this.mCountUpBar2 = (CircularProgressBar) this.findViewById(R.id.countup_bar2);
        mCountUpBar2.setMax(1500);


        NetworkService.getInstance()
                .getJSONApi()
                .getWaterSettings()
                .enqueue(new Callback<WaterSettingsView>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(@NonNull Call<WaterSettingsView> call, @NonNull Response<WaterSettingsView> response) {
                        CommonUtils.hideLoading();
                        if (response.errorBody() == null && response.isSuccessful()) {
                            assert response.body() != null;
                            waterSettings = response.body();

                        } else {
                            waterSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterSettingsView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        waterSettings = null;
                        t.printStackTrace();
                    }
                });


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
                            WaterActivity.this.mCountUpBar2.setProgress(waterView.getWaterVolume());
                        } else {
                            waterView = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        waterView = null;
                        t.printStackTrace();
                    }
                });


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
                            WaterActivity.this.mCountUpBar2.setProgress(waterView.getWaterVolume());
                        } else {
                            waterView = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        waterView = null;
                        t.printStackTrace();
                    }
                });
    }

    public void SetTimer() {

//        calendar2.set(Calendar.HOUR_OF_DAY, 14);
//        calendar2.set(Calendar.MINUTE, 44);
//        calendar2.set(Calendar.SECOND, 0);


        Intent intent = new Intent(this, AlarmReceiverOnBoot.class);
        intent.setAction("NOTIFICATION_INTENT_ACTION_WATER");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        //int i = 10; //sec
        int i = 10;
        int begin = waterSettings.getBegin();
        int end = waterSettings.getEnd();
        Log.d("Bootbegin", Integer.toString(waterSettings.getBegin()));
        Log.d("Bootend", Integer.toString(waterSettings.getEnd()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, 30);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set in " + time.getTime(),
                Toast.LENGTH_LONG).show();


//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (i * 1000), pendingIntent);


        // alarmManager.set(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);

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
}


