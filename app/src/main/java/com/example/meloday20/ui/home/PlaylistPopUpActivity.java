package com.example.meloday20.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meloday20.R;
import com.example.meloday20.models.Post;
import com.example.meloday20.ui.playlist.PlaylistAdapter;
import com.example.meloday20.ui.playlist.PlaylistViewModel;
import com.parse.ParseUser;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class PlaylistPopUpActivity extends AppCompatActivity {
    private static final String TAG = PlaylistPopUpActivity.class.getSimpleName();
    private ParseUser user;
    private String userId;
    private PlaylistViewModel viewModel;
    private RecyclerView rvPlaylistPopUp;
    private PlaylistAdapter playlistAdapter;
    private List<PlaylistTrack> playlistTracks;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvPlaylistPopUpName;
    private TextView tvPlaylistPopUpDescription;
    private ImageView ivPlaylistPopUpCoverImage;
    private TextView tvPlaylistPopUpSongCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_pop_up);
        init();
        displayUsersPlaylist();
    }

    private void displayUsersPlaylist() {
        viewModel.getAnyUserParsePlaylistId(user);
        Observer<String> anyUserPlaylistIdObserver = new Observer<String>() {
            @Override
            public void onChanged(String playlistId) {
                displayPlaylistTracks(playlistId);
                displayPlaylistDetails(playlistId);
            }
        };
        viewModel.anyUserPlaylistId.observe(this, anyUserPlaylistIdObserver);
    }

    private void displayPlaylistTracks(String playlistId) {
        viewModel.getPlaylistTracks(user.getUsername(), playlistId);
        Observer<List<PlaylistTrack>> playlistTracksObserver = new Observer<List<PlaylistTrack>>() {
            @Override
            public void onChanged(List<PlaylistTrack> newTracks) {
                playlistTracks.addAll(newTracks);
                Log.i(TAG, playlistTracks.size() + " songs");
                tvPlaylistPopUpSongCount.setText(playlistTracks.size() + " songs");
                playlistAdapter.notifyDataSetChanged();
            }
        };
        viewModel.playlistTracks.observe(PlaylistPopUpActivity.this, playlistTracksObserver);
    }

    private void displayPlaylistDetails(String playlistId) {
        viewModel.getPlaylistDetails(userId, playlistId);
        viewModel.playlistDetails.observe(PlaylistPopUpActivity.this, new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist playlist) {
                tvPlaylistPopUpName.setText(playlist.name);
                tvPlaylistPopUpDescription.setText(playlist.description);
                if (playlist.images.size() > 0) {
                    // Load in cover image
                    Glide.with(PlaylistPopUpActivity.this)
                            .load(playlist.images.get(0).url)
                            .into(ivPlaylistPopUpCoverImage);
                } else {
                    // Load with default cover image
                    Glide.with(PlaylistPopUpActivity.this)
                            .load(R.drawable.default_playlist_cover)
                            .into(ivPlaylistPopUpCoverImage);
                }
            }
        });
    }

    private void init() {
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        userId = user.getUsername();
        tvPlaylistPopUpName = findViewById(R.id.tvPlaylistPopUpName);
        tvPlaylistPopUpDescription = findViewById(R.id.tvPlaylistPopUpDescription);
        ivPlaylistPopUpCoverImage = findViewById(R.id.ivPlaylistPopUpCoverImage);
        tvPlaylistPopUpSongCount = findViewById(R.id.tvPlaylistPopUpSongCount);
        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        rvPlaylistPopUp = findViewById(R.id.rvPlaylistPopUp);
        playlistTracks = new ArrayList<>();
        playlistAdapter = new PlaylistAdapter(this, playlistTracks);
        linearLayoutManager = new LinearLayoutManager(this);
        rvPlaylistPopUp.setAdapter(playlistAdapter);
        rvPlaylistPopUp.setLayoutManager(linearLayoutManager);
    }
}