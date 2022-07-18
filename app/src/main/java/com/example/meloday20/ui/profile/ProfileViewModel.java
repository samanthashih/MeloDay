package com.example.meloday20.ui.profile;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.meloday20.R;
import com.example.meloday20.models.AlarmTime;
import com.example.meloday20.service.AlarmBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = ProfileViewModel.class.getSimpleName();
    private final ParseUser currentUser = ParseUser.getCurrentUser();
    private Context context;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void createAndSaveNotification(String inputTime) {
        AlarmTime alarmTime = new AlarmTime(inputTime);
        saveAlarmTimeInParse(inputTime);
        createAlarmNotification(alarmTime.getHour(), alarmTime.getMinute());
    }

    private void saveAlarmTimeInParse(String inputTime) {
        currentUser.put(context.getString(R.string.keyAlarmTime), inputTime);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue saving alarm time" , e);
                    return;
                }
                Log.i(TAG, "Alarm time was saved");
            }
        });
    }

    private void createAlarmNotification(int hour, int minute) {
        Log.i(TAG, "Create alarm at: " + hour + ":" + minute);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,  PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void createNotificationChannel() {
        String channelId = context.getString(R.string.channel_id);
        CharSequence channelName = context.getString(R.string.channel_name);
        String channelDescription = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void logoutUser() {
        ParseUser.logOutInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(context.getString(R.string.keyAccessToken), "");
        try {
            currentUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not delete accessToken");
        }
    }
}
