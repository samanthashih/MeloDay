package com.example.meloday20.playlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;

public class PlaylistFragment extends Fragment {
    private static final String TAG = PlaylistFragment.class.getSimpleName();
    private PlaylistViewModel viewModel;
    private Button btnCreatePlaylist;
    private RecyclerView rvPlaylistTracks;
    private ImageView ivPlaylistImage;
    private TextView tvPlaylistName;
    private TextView tvPlaylistDescription;
    private ImageView ivPlaylistProfilePic;
    private TextView tvPlaylistDisplayName;
    private TextView tvPlaylistSongCount;
    private Map<String, Object> createPlaylistParams = new HashMap<>();
    private List<PlaylistTrack> playlistTracks;
    private PlaylistAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);

        viewModel.playlistExists.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean playlistExists) {
                if (playlistExists.booleanValue() == true) {
                    btnCreatePlaylist.setVisibility(View.GONE);
                    displayUserDetails();
                    displayPlaylistDetails();
                    displayPlaylistTracks();
                } else {
                    // set everything but button to gone
                    // create playlist stuff
                }
            }
        });
    }

    private void displayUserDetails() {
        viewModel.getUserDetails();
        viewModel.userDetails.observe(getViewLifecycleOwner(), new Observer<UserPrivate>() {
            @Override
            public void onChanged(UserPrivate user) {
                tvPlaylistDisplayName.setText(user.display_name);
                Glide.with(getContext())
                        .load(user.images.get(0).url)
                        .transform(new RoundedCorners(100))
                        .into(ivPlaylistProfilePic);
            }
        });
    }

    private void displayPlaylistDetails() {
        viewModel.getPlaylistDetails();
        viewModel.playlistDetails.observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist playlist) {
                tvPlaylistName.setText(playlist.name);
                tvPlaylistDescription.setText(playlist.description);
                if (playlist.images.size() > 0) { // load in cover image
                    Glide.with(getContext())
                            .load(playlist.images.get(0).url)
                            .into(ivPlaylistImage);
                } else { // load with default cover image
                    Glide.with(getContext())
                            .load(R.drawable.default_playlist_cover)
                            .into(ivPlaylistImage);
                }
            }
        });
    }

    private void displayPlaylistTracks() {
        viewModel.getPlaylistTracks();
        viewModel.playlistTracks.observe(getViewLifecycleOwner(), new Observer<List<PlaylistTrack>>() {
            @Override
            public void onChanged(List<PlaylistTrack> newPlaylistTracks) {
                playlistTracks.addAll(newPlaylistTracks);
                tvPlaylistSongCount.setText(playlistTracks.size() + " songs");
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initViews(@NonNull View view) {
        btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
        tvPlaylistName = view.findViewById(R.id.tvPlaylistName);
        tvPlaylistDescription = view.findViewById(R.id.tvPlaylistDescription);
        ivPlaylistImage = view.findViewById(R.id.ivPlaylistImage);
        ivPlaylistProfilePic = view.findViewById(R.id.ivPlaylistProfilePic);
        tvPlaylistDisplayName = view.findViewById(R.id.tvPlaylistDisplayName);
        tvPlaylistSongCount = view.findViewById(R.id.tvPlaylistSongCount);
        rvPlaylistTracks = view.findViewById(R.id.rvPlaylistTracks);

        playlistTracks = new ArrayList<>();
        adapter = new PlaylistAdapter(getContext(), playlistTracks);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlaylistTracks.setAdapter(adapter);
        rvPlaylistTracks.setLayoutManager(linearLayoutManager);
    }


//    private void createNewPlaylist() {
//        createPlaylistParams.put("name", displayName + "'s MeloDay");
//        createPlaylistParams.put("description", "Your playlist of the year");
//        createPlaylistParams.put("public", true);
//
//        spotify.createPlaylist(userId, createPlaylistParams, new Callback<Playlist>() {
//            @Override
//            public void success(Playlist playlist, Response response) {
//                Log.d(TAG, playlist.id);
//                playlistId = playlist.id;
//                savePlaylistIdParse();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(TAG, error.toString());
//            }
//        });
//    }

//    private void savePlaylistIdParse() {
//        ParsePlaylist playlist = new ParsePlaylist();
//        playlist.setUser(currentUser);
//        playlist.setPlaylistId(playlistId);
//        playlist.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Parse Error while saving playlistId", e);
//                }}
//        });
//    }
}