package com.example.meloday20.ui.profile;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.TestLooperManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.R;
import com.example.meloday20.ui.login.SpotifyLoginActivity;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ProfileViewModel viewModel;
    private ParseUser currentUser;
    private ImageView ivLogout;
    private ImageView ivProfilePic;
    private TextView tvProfileName;
    private TextView tvTime;
    private Button btnSaveTime;
    private Switch switchReminder;
    private TimePickerDialog timePickerDialog;
    private static String amOrPm;


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
        init(view);
        viewModel.createNotificationChannel();
        displayData();
    }

    private void displayData() {
        switchReminder.setChecked(true);
        tvProfileName.setText(currentUser.getUsername());

        String profilePicUrl = currentUser.getString("profilePicUrl");
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

        tvTime.setText(currentUser.getString(getString(R.string.keyAlarmTime)));
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 12) {
                            amOrPm = getString(R.string.AM);
                        } else {
                            hourOfDay %= 12;
                            amOrPm = getString(R.string.PM);
                        }
                        if (hourOfDay == 0) {
                            hourOfDay = 12;
                        }
                        String time = String.format("%d:%02d", hourOfDay, minute);
                        tvTime.setText(String.format("%s %s", time, amOrPm));
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        btnSaveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputTime = tvTime.getText().toString();
                viewModel.createAndSaveNotification(inputTime);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.logoutUser();
                Intent intent = new Intent(getContext(), SpotifyLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init(@NonNull View view) {
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        currentUser = ParseUser.getCurrentUser();
        ivLogout = view.findViewById(R.id.ivLogout);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvTime = view.findViewById(R.id.tvTime);
        btnSaveTime = view.findViewById(R.id.btnSaveTime);
        switchReminder = view.findViewById(R.id.switchReminder);
        tvProfileName = view.findViewById(R.id.tvProfileName);
    }
}