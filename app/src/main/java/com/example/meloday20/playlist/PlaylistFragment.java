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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.MainActivity;
import com.example.meloday20.R;

import java.util.ArrayList;
import java.util.List;

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
    private List<PlaylistTrack> playlistTracks;
    private PlaylistAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

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
        init(view);

        viewModel.playlistExists.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean playlistExists) {
                if (playlistExists.booleanValue() == true) {
                    btnCreatePlaylist.setVisibility(View.GONE);
                    displayUserDetails();
                    displayPlaylistDetails();
                    displayPlaylistTracks();
                } else {
                    // User has no existing playlist - create a new playlist
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

    private void init(@NonNull View view) {
        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
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

    private void setNoPlaylistExistsVisibility() {
        // Set every view but btnCreatePlaylist to gone
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
                if (user.images.size() > 0) {
                Glide.with(getContext())
                        .load(user.images.get(0).url)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .transform(new RoundedCorners(100))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivPlaylistProfilePic);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.ic_baseline_account_circle_24)
                            .placeholder(R.drawable.ic_baseline_account_circle_24)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(ivPlaylistProfilePic);
                }
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
                if (playlist.images.size() > 0) {
                    // Load in cover image
                    Glide.with(getContext())
                            .load(playlist.images.get(0).url)
                            .into(ivPlaylistImage);
                } else {
                    // Load with default cover image
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

}