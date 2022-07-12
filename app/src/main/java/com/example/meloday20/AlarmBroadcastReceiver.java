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
    private int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = context.getString(R.string.channel_id);

        Notification notif = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("My notification")
                .setContentText("hello world")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notif);
    }
}
