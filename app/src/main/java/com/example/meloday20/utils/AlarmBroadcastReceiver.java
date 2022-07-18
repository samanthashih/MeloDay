package com.example.meloday20.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.meloday20.MainActivity;
import com.example.meloday20.R;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private static String TAG = AlarmBroadcastReceiver.class.getSimpleName();
    private int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String channelId = context.getString(R.string.channel_id);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notif = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle(context.getString(R.string.notificationContentTitle))
                .setContentText(context.getString(R.string.notificationContentText))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, notif);
    }
}
