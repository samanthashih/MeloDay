package com.example.meloday20.playlist;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.MainActivity;
import com.example.meloday20.MeloDayPlaylistTrack;
import com.example.meloday20.MeloDayPlaylistTrackDao;
import com.example.meloday20.MyDatabaseApplication;
import com.example.meloday20.R;
import com.parse.ParseUser;

import org.parceler.Parcels;

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
    private TextView tvPlaylistTextDot;
    private TextView tvPlaylistTextTitle;
    private TextView tvPlaylistTextDate;
    private TextView tvPlaylistTextPublic;
    private View viewDivider;
    private List<MeloDayPlaylistTrack> playlistTracks;
    private PlaylistAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    MeloDayPlaylistTrackDao meloDayPlaylistTrackDao;


    public PlaylistFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meloDayPlaylistTrackDao = ((MyDatabaseApplication) getActivity().getApplicationContext()).getMyDatabase().meloDayPlaylistTrackDao();
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
                    setNoPlaylistExistsVisibility();
                    btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.createNewPlaylist();
                        }
                    });
                }
            }
        });
    }

    private void setNoPlaylistExistsVisibility() {
        btnCreatePlaylist.setVisibility(View.VISIBLE);
        tvPlaylistName.setVisibility(View.GONE);
        tvPlaylistDescription.setVisibility(View.GONE);
        ivPlaylistImage.setVisibility(View.GONE);
        ivPlaylistProfilePic.setVisibility(View.GONE);
        tvPlaylistDisplayName.setVisibility(View.GONE);
        tvPlaylistSongCount.setVisibility(View.GONE);
        rvPlaylistTracks.setVisibility(View.GONE);
        tvPlaylistTextDot.setVisibility(View.GONE);
        tvPlaylistTextTitle.setVisibility(View.GONE);
        tvPlaylistTextDate.setVisibility(View.GONE);
        tvPlaylistTextPublic.setVisibility(View.GONE);
        viewDivider.setVisibility(View.GONE);
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
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Showing playlist tracks data from db");
                List<MeloDayPlaylistTrack> meloDayPlaylistTracks = viewModel.getMeloDayPlaylistTracksFromDb();
                playlistTracks.addAll(meloDayPlaylistTracks);
                adapter.notifyDataSetChanged();
            }
        });

//        viewModel.getPlaylistTracks();
//        viewModel.playlistTracks.observe(getViewLifecycleOwner(), new Observer<List<MeloDayPlaylistTrack>>() {
//            @Override
//            public void onChanged(List<MeloDayPlaylistTrack> newPlaylistTracks) {
//                playlistTracks.addAll(newPlaylistTracks);
//                tvPlaylistSongCount.setText(playlistTracks.size() + " songs");
//                adapter.notifyDataSetChanged();
//            }
//        });
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
        tvPlaylistTextDot = view.findViewById(R.id.tvPlaylistTextDot);
        tvPlaylistTextTitle = view.findViewById(R.id.tvPlaylistTextTitle);
        tvPlaylistTextDate = view.findViewById(R.id.tvPlaylistTextDate);
        tvPlaylistTextPublic = view.findViewById(R.id.tvPlaylistTextPublic);
        viewDivider = view.findViewById(R.id.viewDivider);

        playlistTracks = new ArrayList<>();
        adapter = new PlaylistAdapter(getContext(), playlistTracks);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPlaylistTracks.setAdapter(adapter);
        rvPlaylistTracks.setLayoutManager(linearLayoutManager);
    }
}