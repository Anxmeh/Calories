package com.example.caloriescounter.network.utils;

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

import androidx.annotation.RequiresApi;

import com.example.caloriescounter.MainActivity;
import com.example.caloriescounter.R;
import com.example.caloriescounter.WaterActivity;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class AlarmReceiver  extends BroadcastReceiver  {
    NotificationManager notificationManager;


//    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("Receiver", "After Water in Receiver");
        Log.d(TAG,"Intent:"+ action);
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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
      //  mNM.notify(R.string.alarm_service_label, notification);
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
}
