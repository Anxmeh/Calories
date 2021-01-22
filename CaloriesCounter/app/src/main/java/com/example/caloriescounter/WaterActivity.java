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

public class WaterActivity extends BaseActivity {

    private static final String TAG = WaterActivity.class.getSimpleName();
    private static final int ONE_SECOND_IN_MS = 1000;
    private CircularProgressBar mCountUpBar2;

    private CountDownTimer mTimerCountDown;
    private CountDownTimer mTimerCountUp;


    private RadioGroup radioGroupWater;
    private TextView txtWaterCount;
    private final Calendar calendar = Calendar.getInstance();
    private final Calendar calendar2 = Calendar.getInstance();
    private Date currentTime;
    private WaterView waterView;
    private WaterSettingsView waterSettings;

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
        txtWaterCount = findViewById(R.id.txtWaterCount);
        currentTime = Calendar.getInstance().getTime();


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


        radioGroupWater = (RadioGroup) findViewById(R.id.radioGroupWater);
        radioGroupWater.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.radio100:
                        wat = 100;
                        break;
                    case R.id.radio200:
                        wat = 200;
                        break;
                    case R.id.radio300:
                        wat = 300;
                        break;
                    case R.id.radio400:
                        wat = 400;
                        break;
                    case R.id.radio500:
                        wat = 500;
                        break;
                    default:
                        break;
                }
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
                            txtWaterCount.setText(Integer.toString(waterView.getWaterVolume()));
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

    //    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickAddWater(View view) {

        calendar2.set(Calendar.HOUR_OF_DAY, 17);
        calendar2.set(Calendar.MINUTE, 50);
        calendar2.set(Calendar.SECOND, 0);


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
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);

//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);

//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

//calendar2.getTime().toString();

        Toast.makeText(this, "Alarm set in " + i + " seconds",
                Toast.LENGTH_LONG).show();
//        int notificationID = 101;
//        Intent resultIntent = new Intent(this, ResultActivity.class);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        String channelID = "a.notifydemo.news";
//
//        final Icon icon = Icon.createWithResource(WaterActivity.this, android.R.drawable.ic_dialog_info);
//        Notification.Action action = new Notification.Action.Builder(icon, "Open", pendingIntent)
//                .build();
//
//        Notification notification = new Notification.Builder(WaterActivity.this, channelID)
//                .setContentTitle("New Message")
//                .setContentText("You've received new messages.")
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setChannelId(channelID)
//                .setNumber(10)
//                .setContentIntent(pendingIntent)
//                .setActions(action)
//                .build();
//        notificationManager.notify(notificationID, notification);

//

    }

    public void onClickAddWater1(View view) {
        final WaterView model = new WaterView();
//        int i = 200;
//        RadioGroup radioGroupWater2;
//        radioGroupWater2 = (RadioGroup) findViewById(R.id.radioGroupWater);
//        radioGroupWater2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup arg0, int id) {
//                switch (id) {
//                    case R.id.radio100:
//                        water2 = 100;
//                        break;
//                    case R.id.radio200:
//                        water2 = 200;
//                        break;
//                    case R.id.radio300:
//                        water2 = 300;
//                        break;
//                    case R.id.radio400:
//                        water2 = 400;
//                        break;
//                    case R.id.radio500:
//                        water2 = 500;
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//
//        Toast.makeText(this, "Alarm set in " + i + " seconds",
//                Toast.LENGTH_LONG).show();
        model.setDateOfDrink(currentTime);
        model.setWaterVolume(wat);

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
                            txtWaterCount.setText(Integer.toString(waterView.getWaterVolume()));
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
}


