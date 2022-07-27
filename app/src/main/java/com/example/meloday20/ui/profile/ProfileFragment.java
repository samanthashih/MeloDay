package com.example.meloday20.ui.profile;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
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
import com.example.meloday20.utils.CommonActions;
import com.parse.ParseUser;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private ProfileViewModel viewModel;
    private ParseUser currentUser;
    private ImageView ivLogout;
    private ImageView ivProfilePic;
    private TextView tvProfileName;
    private TextView tvTime;
    private Button btnSaveTime;
    private TextView tvProfileBio;
    private Switch switchReminder;
    private TimePickerDialog timePickerDialog;
    private static String amOrPm;
    private PieChart pieChart;
    private Resources resources;
    private String[] colors;


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
        tvProfileName.setText(CommonActions.getUserId());
        tvProfileBio.setText(CommonActions.getBio());

        String profilePicUrl = CommonActions.getProfilePicUrl();
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

        viewModel.getCurrUserPlaylistGenres(colors);
        viewModel.pieModels.observe(getViewLifecycleOwner(), new Observer<List<PieModel>>() {
            @Override
            public void onChanged(List<PieModel> pieModels) {
                Log.i(TAG, "num of pie models: " + pieModels.size());
                for (PieModel model : pieModels) {
                    pieChart.addPieSlice(model);
                }
            }
        });
        pieChart.startAnimation();

//        Log.i(TAG, "final map: " + genreCountMap.toString());
//        for (Map.Entry<String, Integer> entry : genreCountMap.entrySet()) {
//            String genre = entry.getKey();
//            Integer count = entry.getValue();
//            pieChart.addPieSlice(
//                    new PieModel(
//                            genre,
//                            count,
//                            Color.parseColor("#FFA726")));
//        }
    }


    private void init(@NonNull View view) {
        resources = getActivity().getResources();

        //noinspection ResourceType
        colors = new String[] {
                resources.getString(R.color.color_one),
                resources.getString(R.color.color_two),
                resources.getString(R.color.color_three),
                resources.getString(R.color.color_four),
                resources.getString(R.color.color_five),
                resources.getString(R.color.color_six),
                resources.getString(R.color.color_seven),
                resources.getString(R.color.color_eight),
                resources.getString(R.color.color_nine),
                resources.getString(R.color.color_ten),
        };

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        currentUser = ParseUser.getCurrentUser();
        ivLogout = view.findViewById(R.id.ivLogout);
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvTime = view.findViewById(R.id.tvTime);
        btnSaveTime = view.findViewById(R.id.btnSaveTime);
        switchReminder = view.findViewById(R.id.switchReminder);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileBio = view.findViewById(R.id.tvProfileBio);
        pieChart = view.findViewById(R.id.piechart);
    }
}