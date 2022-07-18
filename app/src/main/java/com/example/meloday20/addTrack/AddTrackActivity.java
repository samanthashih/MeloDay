package com.example.meloday20.addTrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieDrawable;
import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.example.meloday20.MainActivity;
import com.example.meloday20.R;

import org.parceler.Parcels;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

public class AddTrackActivity extends AppCompatActivity {
    private static final String TAG = AddTrackActivity.class.getSimpleName();
    private static AddTrackViewModel viewModel;
    private static Track track;
    private TextView tvAddTrackTitle;
    private TextView tvAddTrackArtist;
    private ImageView ivAddTrackCover;
    private Button btnAddTrackToPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        init();
        setViewValues();
    }
    private void init() {
        viewModel = new ViewModelProvider(this).get(AddTrackViewModel.class);
        track = Parcels.unwrap(getIntent().getParcelableExtra(getString(R.string.keyTrack)));

        tvAddTrackTitle = findViewById(R.id.tvAddTrackTitle);
        tvAddTrackArtist = findViewById(R.id.tvAddTrackArtist);
        ivAddTrackCover = findViewById(R.id.ivAddTrackCover);
        btnAddTrackToPlaylist = findViewById(R.id.btnAddTrackToPlaylist);

        btnAddTrackToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.checkIfPostedToday();
                Observer<Boolean> postedTodayObserver = new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean postedToday) {
                        if (postedToday) {
                            displayAlreadyPostedMessage();
                        }
                        else {
                            viewModel.addTrackActions(track);
                            goToMainActivity();
                        }
                    }
                };
                viewModel.postedToday.observe(AddTrackActivity.this, postedTodayObserver);
            }
        });
    }

    private void setViewValues() {
        tvAddTrackTitle.setText(track.name);

        // Format string to display list of artists
        String artistsString = track.artists.get(0).name;
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

    private void goToMainActivity() {
        Intent intent = new Intent(AddTrackActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayAlreadyPostedMessage() {
        Button btnOk = new Button(this);
        btnOk.setText("Ok");
        btnOk.setTextColor(Color.parseColor(getString(R.string.colorWhite)));

        LottieDialog dialog = new LottieDialog(this)
                .setAnimation(R.raw.night_car_driving)
                .setAnimationRepeatCount(LottieDrawable.INFINITE)
                .setAutoPlayAnimation(true)
                .setDialogBackground(Color.parseColor(getString(R.string.colorSpotifyGray)))
                .setMessage(getString(R.string.alreadyPostedMessage))
                .setMessageTextSize(18)
                .addActionButton(btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}