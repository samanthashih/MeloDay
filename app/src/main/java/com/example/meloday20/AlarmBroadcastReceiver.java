package com.example.meloday20;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = AlarmBroadcastReceiver.class.getSimpleName();
    private static final String CHANNEL_ID = "myChannelId";
    private int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification notif = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("My notification")
                .setContentText("hello world")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, notif);

//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//                        .setContentTitle("My notification")
//                        .setPriority(NotificationCompat.PRIORITY_MAX)
//                        .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                        .setContentIntent(pendingIntent)
//                        .setContentText("hello world")
//                        .setAutoCancel(true)
//                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//
//
//        Log.i(TAG, "Notification");
//        mNotificationManager.notify(notificationId, mBuilder.build());
    }
}
