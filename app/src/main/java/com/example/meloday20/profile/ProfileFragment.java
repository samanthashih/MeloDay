package com.example.meloday20.profile;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.AlarmBroadcastReceiver;
import com.example.meloday20.R;
import com.example.meloday20.login.SpotifyLoginActivity;
import com.example.meloday20.utils.GetDetails;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private String channelId;
    private CharSequence name;
    private String description;
    private int importance;
    ParseUser currentUser;
    ImageView ivLogout;
    ImageView ivProfilePic;
    TextView tvTime;
    Button btnSaveTime;
    TimePickerDialog timePickerDialog;
    String amOrPm;
    String profilePicUrl;
    String userAlarm;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        channelId = getContext().getString(R.string.channel_id);
        name = getContext().getString(R.string.channel_name);
        description = getContext().getString(R.string.channel_description);
        importance = NotificationManager.IMPORTANCE_HIGH;
        ivLogout = view.findViewById(R.id.ivLogout);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvTime = view.findViewById(R.id.tvTime);
        btnSaveTime = view.findViewById(R.id.btnSaveTime);
        createNotificationChannel();

        profilePicUrl = currentUser.getString("profilePicUrl");
        if (profilePicUrl != null) {
            Glide.with(getContext())
                    .load(profilePicUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .transform(new RoundedCorners(300))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivProfilePic);
        } else {
            Glide.with(getContext())
                    .load(R.drawable.ic_baseline_account_circle_24)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivProfilePic);
        }

        tvTime.setText(currentUser.getString("alarmTime"));

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 12) {
                            amOrPm = "AM";
                        } else {
                            hourOfDay %= 12;
                            amOrPm = "PM";
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String time = String.format("%d:%02d", hourOfDay, minute);
                        tvTime.setText(time + " " + amOrPm);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTime = tvTime.getText().toString();
//                Log.i(TAG, "inputted time: " + time);
                AlarmTime alarmTime = new AlarmTime(inputTime);
                saveAlarmTimeInParse(inputTime);
                createAlarmNotif(alarmTime.hour, alarmTime.minute);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOutInBackground();

                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("accessToken", "");
                try {
                    currentUser.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Could not delete accessToken");

                }
//                currentUser.saveInBackground(new SaveCallback() {
//                    @Override
//                    public void done(ParseException e) {
//                        if (e != null) {
//                            Log.e(TAG, "Could not delete accessToken");
//                        }
//                    }
//                });
                Intent intent = new Intent(getContext(), SpotifyLoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void saveAlarmTimeInParse(String inputTime) {
        currentUser.put("alarmTime", inputTime);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue saving alarm time" , e);
                    return;
                }
                Log.i(TAG, "Alarm time was saved!!");
            }
        });
    }

    private void createAlarmNotif(int hour, int minute) {
        Log.i(TAG, "Create alarm at: " + hour + ":" + minute);
        Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent,  0);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}