package com.example.caloriescounter.network.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.caloriescounter.WaterActivity;
import com.example.caloriescounter.models.WaterTimeView;
import com.example.caloriescounter.network.NetworkService;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiverOnBoot extends BroadcastReceiver {
    NotificationManager notificationManager;
    Calendar calendar2 = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    Date currentTime;
    WaterTimeView waterSettings;
    int beginHour, beginMinute, endHour, endMinute;

    @Override
    public void onReceive(Context context, Intent intent) {
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
                            beginHour = waterSettings.getBeginHour();
                            endHour = waterSettings.getEndHour();
                            beginMinute = waterSettings.getBeginMinute();
                            endMinute = waterSettings.getEndMinute();
                            Log.d("Bootbegin", waterSettings.getBeginHour() + " : " + waterSettings.getBeginMinute());
                            Log.d("Bootend", waterSettings.getEndHour() + " : " + waterSettings.getEndMinute());
                            timerOnBoot(context, intent);
                        } else {
                            waterSettings = null;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<WaterTimeView> call, @NonNull Throwable t) {
                        CommonUtils.hideLoading();
                        waterSettings = null;
                        t.printStackTrace();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    private void timerOnBoot(Context context, Intent intent) {
        currentTime = Calendar.getInstance().getTime();
        String action = intent.getAction();
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_USER_PRESENT)) {
            Intent intent2 = new Intent(context.getApplicationContext(), AlarmReceiverOnBoot.class);
            intent2.setAction("NOTIFICATION_INTENT_ACTION_WATER");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                    234324243, intent2, 0);
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);

            if (hour < beginHour) {
                calendar2.set(Calendar.HOUR_OF_DAY, beginHour);
                calendar2.set(Calendar.MINUTE, beginMinute);
                calendar2.set(Calendar.SECOND, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
            } else if (hour >= beginHour && hour < endHour) {
                calendar2.set(Calendar.HOUR_OF_DAY, hour + 1);
                calendar2.set(Calendar.MINUTE, 1);
                calendar2.set(Calendar.SECOND, 0);
                Log.d("in if between", calendar2.toString());
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
            } else if (hour >= endHour) {

                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                calendar2.set(Calendar.HOUR_OF_DAY, beginHour);
                calendar2.set(Calendar.MINUTE, beginMinute);
                calendar2.set(Calendar.SECOND, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
            }

            Log.d("Boot", "Alarm set in " + calendar2.getTime().toString());
            String channelID = "a.notifydemo.news";
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
        } else if (action.equals("NOTIFICATION_INTENT_ACTION_WATER")) {
            notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                createNotificationChannel("a.notifydemo.news",
//                        "NotifyDemo News", "Example News Channel");
//            }
//            String channelID = "a.notifydemo.news";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("a.notifydemo.calories",
                        "CaloriesCounter Reminder", "CaloriesCounter Reminder");
            }
            String channelID = "a.notifydemo.calories";

            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
            Notification.Builder builder = null;
            Log.d("nen", "nen"+ System.currentTimeMillis() + " " + calendar.getTimeInMillis());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(context, channelID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                        .setContentTitle("Нагадування")
                        .setContentText("Час випити води!")
                        .setContentIntent(contentIntent)
                                                .setAutoCancel(true)
                        ;
            }

            Notification notification = builder.build();
            int notificationId = 103;
            notificationManager.notify(notificationId, notification);
        }
    }
}
