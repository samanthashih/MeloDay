package com.example.meloday20.addTrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meloday20.MainActivity;
import com.example.meloday20.R;
import com.example.meloday20.home.Post;
import com.example.meloday20.playlist.ParsePlaylist;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class AddTrackActivity extends AppCompatActivity {
    private static final String TAG = AddTrackActivity.class.getSimpleName();
    private AddTrackViewModel viewModel;
    private static Track track;
    private static String artistsString;
    TextView tvAddTrackTitle;
    TextView tvAddTrackArtist;
    ImageView ivAddTrackCover;
    Button btnAddTrackToPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        viewModel = new ViewModelProvider(this).get(AddTrackViewModel.class);
        track = Parcels.unwrap(getIntent().getParcelableExtra("track"));
        initViews();
        setViewValues();
    }

    private void setViewValues() {
        tvAddTrackTitle.setText(track.name);
        artistsString = track.artists.get(0).name;
        if (track.artists.size() > 1) {
            for (int i = 1; i < track.artists.size(); i++) {
                artistsString = artistsString + ", " + track.artists.get(i).name;
            }
        }
        tvAddTrackArtist.setText(artistsString);
        Image coverImage = track.album.images.get(0);
        if (coverImage != null) {
            Glide.with(this)
                    .load(coverImage.url)
                    .into(ivAddTrackCover);
        }
    }

    private void initViews() {
        tvAddTrackTitle = findViewById(R.id.tvAddTrackTitle);
        tvAddTrackArtist = findViewById(R.id.tvAddTrackArtist);
        ivAddTrackCover = findViewById(R.id.ivAddTrackCover);
        btnAddTrackToPlaylist = findViewById(R.id.btnAddTrackToPlaylist);
        btnAddTrackToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.addTrackActions(track);
                goToMainActivity();
                //todo: uncomment out, code for limit once a day
//                viewModel.checkIfPostedToday();
//                Observer<Boolean> postedTodayObserver = new Observer<Boolean>() {
//                    @Override
//                    public void onChanged(Boolean postedToday) {
//                        if (postedToday) {
//                            displayAlreadyPostedMessage();
//                        }
//                        else {
//                            viewModel.addTrackActions(track);
//                            goToMainActivity();
//                        }
//                    }
//                };
//                viewModel.postedToday.observe(AddTrackActivity.this, postedTodayObserver);
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AddTrackActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayAlreadyPostedMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setCancelable(true);
        builder.setTitle("You already posted today!");
        builder.setMessage("You may only post one song per day. Delete current post?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    viewModel.deleteTodayPost();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.cancel();
            }
        });
        builder.show();
    }
}