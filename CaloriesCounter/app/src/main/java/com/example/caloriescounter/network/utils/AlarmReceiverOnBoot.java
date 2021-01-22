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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.caloriescounter.WaterActivity;
import com.example.caloriescounter.models.WaterSettingsView;
import com.example.caloriescounter.models.WaterView;
import com.example.caloriescounter.network.NetworkService;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

//переробити з можливістю звичайного ресівера, і перевіркою на загрузку

public class AlarmReceiverOnBoot extends BroadcastReceiver  {
    NotificationManager notificationManager;
    Calendar calendar2 = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    Date currentTime;
    WaterSettingsView waterSettings;
    int begin;
    int end;

    @Override
    public void onReceive(Context context, Intent intent)
    {
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
                            begin = waterSettings.getBegin();
                            end = waterSettings.getEnd();
                            Log.d("Bootbegin", Integer.toString(waterSettings.getBegin()));
                            Log.d("Bootend", Integer.toString(waterSettings.getEnd()));
//                            Intent intent2 = new Intent(context.getApplicationContext(), AlarmReceiverOnBoot.class);
//                            intent2.setAction("NOTIFICATION_INTENT_ACTION_WATER");
//                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
//                                    234324243, intent2, 0);
                            timerOnBoot(context, intent);
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



//        currentTime = Calendar.getInstance().getTime();
//        String action = intent.getAction();
//        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
//        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_USER_PRESENT) ) {
//
//            Log.d("Boot", "After Boot");
//            Log.d(TAG,"Intent:"+ action);
//
//
//            Intent intent2 = new Intent(context.getApplicationContext(), AlarmReceiverOnBoot.class);
//        intent2.setAction("NOTIFICATION_INTENT_ACTION_WATER");
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
//                    234324243, intent2, 0);
//           Calendar now = Calendar.getInstance();
//            int hour = now.get(Calendar.HOUR_OF_DAY);
//
//            if (hour < begin){
//                calendar2.set(Calendar.HOUR_OF_DAY, 9);
//                calendar2.set(Calendar.MINUTE, 1);
//                calendar2.set(Calendar.SECOND, 0);
//                Log.d("Calendar after", calendar2.toString());
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
//                }
//            }
//            else if (hour > 9 && hour < 22) {
//                calendar2.set(Calendar.HOUR_OF_DAY, hour+1);
//                calendar2.set(Calendar.HOUR_OF_DAY, 18);
//                calendar2.set(Calendar.MINUTE, 11);
//                calendar2.set(Calendar.SECOND, 0);
//                Log.d("in if between", calendar2.toString());
//                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
//                }
//            }
//            else{
//                Log.d("in if else", calendar2.toString());
//            }
//
//            Log.d("Boot", "Alarm set in " + calendar2.getTime().toString() + " seconds");
//            //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)startTime, intent, 0);
//           // int i = 12;
////            calendar2.set(Calendar.HOUR_OF_DAY, 16);
////            calendar2.set(Calendar.MINUTE, 47);
////            calendar2.set(Calendar.SECOND, 0);
////            Log.d("Calendar after", calendar2.toString());
////            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//////            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//////                    + (12000), pendingIntent);
////
////            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
////                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
//
//
//            String channelID = "a.notifydemo.news";
//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            // builder.setSound(alarmSound);
//
//            //show only in period
//            if (calendar.get(Calendar.HOUR_OF_DAY) > 18) {
//                Notification.Builder builder3 = null;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    builder3 = new Notification.Builder(context, channelID)
//                            .setSmallIcon(android.R.drawable.ic_dialog_info)
//                            .setContentTitle("New Message")
//                            .setContentText("Time to drink water! From boot")
//                            .setContentIntent(contentIntent)
//                            //  .setDefaults(Notification.DEFAULT_SOUND)
//                            // .setSound(alarmSound)
//                            .setWhen(System.currentTimeMillis());
//                }
//
//                Notification notification2 = builder3.build();
//                // Set the info for the views that show in the notification panel.
//                //notification2.setLatestEventInfo(context, context.getText(R.string.alarm_service_label), "This is a Test Alarm", contentIntent);
//                // Send the notification.
//                // We use a layout id because it is a unique number. We use it later to cancel.
//                int notificationId3 = 103;
//                notificationManager.notify(notificationId3, notification2);
//            }
//
//
//
//
//
//
//
//
//
//        }
//        else if (action.equals("NOTIFICATION_INTENT_ACTION_WATER")) {
//            String actionWater = intent.getAction();
//            Log.d("Boot", "After Water in Boot");
//            Log.d(TAG,"Intent:"+ actionWater);
////        Intent intent1 = new Intent(context, MainActivity.class);
////        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////        context.startActivity(intent1);
//
//            //NotificationManager mNM;
//            notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                createNotificationChannel("a.notifydemo.news",
//                        "NotifyDemo News", "Example News Channel");
//            }
//            String channelID = "a.notifydemo.news";
//
//            // Set the icon, scrolling text and timestamp
////        Notification.Builder builder3 = new Notification.Builder(context, channelID)
////                .setSmallIcon(android.R.drawable.ic_dialog_info)
////                .setContentTitle("New Message")
////                .setContentText("You have a new message from Jason")
////                .setWhen(System.currentTimeMillis());
////        Notification notification2 = builder3.build();
//
////        Notification notification = new Notification(R.mipmap.ic_launcher, "Test Alarm",
////                System.currentTimeMillis());
//
//
//            // The PendingIntent to launch our activity if the user selects this notification
//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
//            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            // builder.setSound(alarmSound);
//            Notification.Builder builder3 = null;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                builder3 = new Notification.Builder(context, channelID)
//                        .setSmallIcon(android.R.drawable.ic_dialog_info)
//                        .setContentTitle("New Message")
//                        .setContentText("Time to drink water!")
//                        .setContentIntent(contentIntent)
//                        //  .setDefaults(Notification.DEFAULT_SOUND)
//                        // .setSound(alarmSound)
//                        .setWhen(System.currentTimeMillis());
//            }
//
//            Notification notification2 = builder3.build();
//            // Set the info for the views that show in the notification panel.
//            //notification2.setLatestEventInfo(context, context.getText(R.string.alarm_service_label), "This is a Test Alarm", contentIntent);
//            // Send the notification.
//            // We use a layout id because it is a unique number. We use it later to cancel.
//            int notificationId3 = 103;
//            notificationManager.notify(notificationId3, notification2);
//        }
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

    protected void setTimeNotifacations() {

            calendar2.set(Calendar.HOUR_OF_DAY, 14);
            calendar2.set(Calendar.MINUTE, 42);
            calendar2.set(Calendar.SECOND, 0);
        Log.d("Calendar", calendar2.toString());


//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

        /// тут буде отримання часу з бази
//            NetworkService.getInstance()
//                    .getJSONApi()
//                    .getWater(calendar2.getTime())
//                    .enqueue(new Callback<WaterView>() {
//                        @SuppressLint("SetTextI18n")
//                        @Override
//                        public void onResponse(@NonNull Call<WaterView> call, @NonNull Response<WaterView> response) {
//                            CommonUtils.hideLoading();
//                            if (response.errorBody() == null && response.isSuccessful()) {
//                                assert response.body() != null;
//                                //  waterView = response.body();
//                                // txtWaterCount.setText(Integer.toString(waterView.getWaterVolume()));
//                                // WaterActivity.this.mCountUpBar2.setProgress(waterView.getWaterVolume());
//                            } else {
//                                //  waterView = null;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<WaterView> call, @NonNull Throwable t) {
//                            //CommonUtils.hideLoading();
//                            //    waterView = null;
//                            t.printStackTrace();
//                        }
//                    });
    }
    private void timerOnBoot(Context context, Intent intent) {
        currentTime = Calendar.getInstance().getTime();
        String action = intent.getAction();
        notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        if (action.equals(Intent.ACTION_BOOT_COMPLETED) || action.equals(Intent.ACTION_USER_PRESENT) ) {

            Log.d("Boot", "After Boot");
            Log.d(TAG,"Intent:"+ action);


            Intent intent2 = new Intent(context.getApplicationContext(), AlarmReceiverOnBoot.class);
            intent2.setAction("NOTIFICATION_INTENT_ACTION_WATER");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                    234324243, intent2, 0);
            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);

            if (hour < begin){
                calendar2.set(Calendar.HOUR_OF_DAY, 9);
                calendar2.set(Calendar.MINUTE, 1);
                calendar2.set(Calendar.SECOND, 0);
                Log.d("Calendar after", calendar2.toString());
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
            }
            else if (hour > begin && hour < end) {
                calendar2.set(Calendar.HOUR_OF_DAY, hour+1);
//                calendar2.set(Calendar.HOUR_OF_DAY, 18);
                calendar2.set(Calendar.MINUTE, 1);
                calendar2.set(Calendar.SECOND, 0);
                Log.d("in if between", calendar2.toString());
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
            }
            else if (hour > 22){

                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR)+1 );
                calendar2.set(Calendar.HOUR_OF_DAY, 9);
//                calendar2.set(Calendar.HOUR_OF_DAY, 18);
                calendar2.set(Calendar.MINUTE, 1);
                calendar2.set(Calendar.SECOND, 0);
                Log.d("in if between", calendar2.toString());
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                }
                Log.d("in if else", calendar2.toString());
            }

            Log.d("Boot", "Alarm set in " + calendar2.getTime().toString() + " seconds");
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)startTime, intent, 0);
            // int i = 12;
//            calendar2.set(Calendar.HOUR_OF_DAY, 16);
//            calendar2.set(Calendar.MINUTE, 47);
//            calendar2.set(Calendar.SECOND, 0);
//            Log.d("Calendar after", calendar2.toString());
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
////                    + (12000), pendingIntent);
//
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


            String channelID = "a.notifydemo.news";
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // builder.setSound(alarmSound);

            //show only in period
            if (calendar.get(Calendar.HOUR_OF_DAY) > 18) {
                Notification.Builder builder3 = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder3 = new Notification.Builder(context, channelID)
                            .setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("New Message")
                            .setContentText("Time to drink water! From boot")
                            .setContentIntent(contentIntent)
                            //  .setDefaults(Notification.DEFAULT_SOUND)
                            // .setSound(alarmSound)
                            .setWhen(System.currentTimeMillis());
                }

                Notification notification2 = builder3.build();
                // Set the info for the views that show in the notification panel.
                //notification2.setLatestEventInfo(context, context.getText(R.string.alarm_service_label), "This is a Test Alarm", contentIntent);
                // Send the notification.
                // We use a layout id because it is a unique number. We use it later to cancel.
                int notificationId3 = 103;
                notificationManager.notify(notificationId3, notification2);
            }









        }
        else if (action.equals("NOTIFICATION_INTENT_ACTION_WATER")) {
            String actionWater = intent.getAction();
            Log.d("Boot", "After Water in Boot");
            Log.d(TAG,"Intent:"+ actionWater);
//        Intent intent1 = new Intent(context, MainActivity.class);
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);

            //NotificationManager mNM;
            notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("a.notifydemo.news",
                        "NotifyDemo News", "Example News Channel");
            }
            String channelID = "a.notifydemo.news";

            // Set the icon, scrolling text and timestamp
//        Notification.Builder builder3 = new Notification.Builder(context, channelID)
//                .setSmallIcon(android.R.drawable.ic_dialog_info)
//                .setContentTitle("New Message")
//                .setContentText("You have a new message from Jason")
//                .setWhen(System.currentTimeMillis());
//        Notification notification2 = builder3.build();

//        Notification notification = new Notification(R.mipmap.ic_launcher, "Test Alarm",
//                System.currentTimeMillis());


            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, WaterActivity.class), 0);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            // builder.setSound(alarmSound);
            Notification.Builder builder3 = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder3 = new Notification.Builder(context, channelID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle("New Message")
                        .setContentText("Time to drink water!")
                        .setContentIntent(contentIntent)
                        //  .setDefaults(Notification.DEFAULT_SOUND)
                        // .setSound(alarmSound)
                        .setWhen(System.currentTimeMillis());
            }

            Notification notification2 = builder3.build();
            // Set the info for the views that show in the notification panel.
            //notification2.setLatestEventInfo(context, context.getText(R.string.alarm_service_label), "This is a Test Alarm", contentIntent);
            // Send the notification.
            // We use a layout id because it is a unique number. We use it later to cancel.
            int notificationId3 = 103;
            notificationManager.notify(notificationId3, notification2);
        }
    }
}
