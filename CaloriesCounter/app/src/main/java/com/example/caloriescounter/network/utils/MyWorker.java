//package com.example.caloriescounter.network.utils;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Icon;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//import com.example.caloriescounter.R;
//import com.example.caloriescounter.ResultActivity;
//import com.example.caloriescounter.WaterActivity;
//
//import java.util.concurrent.TimeUnit;
//
//public class MyWorker extends Worker {
//    NotificationManager notificationManager;
//    static final String TAG = "workmng";
//    private static final String CHANNEL_ID ="com.chikeandroid.tutsplustalerts.ANDROID" ;
//    Context mContext;
//
//    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
//        super(context, workerParams);
//        mContext = context;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @NonNull
//    @Override
//    public Result  doWork() {
//        Log.d(TAG, "doWork: start");
//
//        try {
//            createNotificationChannel("a.notifydemo.news",
//                    "NotifyDemo News", "Example News Channel");
//            String channelID = "a.notifydemo.news";
//            int notificationId3 = 103;
//            Notification.Builder builder3 = new Notification.Builder(getApplicationContext(), channelID)
//                    .setSmallIcon(android.R.drawable.ic_dialog_info)
//                    .setContentTitle("New Message")
//                    .setContentText("You have a new message from Jason");
//            notificationManager.notify(notificationId3, builder3.build());
//
//
//
//
//
//
//
////            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////
////            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel",
////                        NotificationManager.IMPORTANCE_HIGH);
////                channel.setDescription("My channel description");
////                channel.enableLights(true);
////                channel.setLightColor(Color.RED);
////                channel.enableVibration(false);
////                notificationManager.createNotificationChannel(channel);
////            }
//
////            NotificationCompat.Builder builder1 =
////                    new NotificationCompat.Builder(this)
////                            .setSmallIcon(R.mipmap.ic_launcher)
////                            .setContentTitle("Title")
////                            .setContentText("Notification text");
////
////            Notification notification = builder.build();
////
////            NotificationManager notificationManager =
////                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////            notificationManager.notify(1, notification);
//
//
////            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
////                    .setSmallIcon(R.drawable.ic_brush)
////                    .setContentTitle("textTitle")
////                    .setContentText("textContent")
////                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//
//
//
//
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, "doWork: end");
//        return Result.success();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    protected void createNotificationChannel(String id, String name, String description) {
//        int importance = NotificationManager.IMPORTANCE_LOW;
//        NotificationChannel channel = new NotificationChannel(id, name, importance);
//
//        channel.setDescription(description);
//        channel.enableLights(true);
//        channel.setLightColor(Color.RED);
//        channel.enableVibration(true);
//        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//        notificationManager.createNotificationChannel(channel);
//    }
//}
