package com.example.meloday20.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meloday20.MainActivity;
import com.example.meloday20.ParseApplication;
import com.example.meloday20.R;
import com.example.meloday20.SpotifyLoginActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spotify.api.spotify.SpotifyApi;
import spotify.models.paging.Paging;
import spotify.models.playlists.PlaylistSimplified;
import spotify.models.playlists.requests.CreateUpdatePlaylistRequestBody;

public class PlaylistFragment extends Fragment {
    private static final String TAG = "PlaylistFragment";
    private String accessToken;
    private SpotifyApi spotifyApi;
    private String userId;
    private String displayName;
    private Button btnCreatePlaylist;
    Map<String, String> getPlaylistsParams = new HashMap<>();
    private String playlistId;


    public PlaylistFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accessToken = getArguments().getString("accessToken");
        }
        spotifyApi = new SpotifyApi(accessToken);
        new setUserDetails().execute();
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
        btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);

        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createPlaylist().execute();
            }
        });
    }

    // Spotify API Tasks
    private class createPlaylist extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            setNewPlaylistId();
            parseSavePlaylistId();
            return null;
        }

        private void setNewPlaylistId() {
            CreateUpdatePlaylistRequestBody body = new CreateUpdatePlaylistRequestBody(displayName + "'s meloday", "my description", true, false);
            spotifyApi.createPlaylist(userId, body);
            getPlaylistsParams.put("limit", "1");
            PlaylistSimplified playlist =  spotifyApi.getPlaylists(getPlaylistsParams).getItems().get(0);
            playlistId = playlist.getId();
        }

        private void parseSavePlaylistId() {
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.put("playlistId", playlistId);
            currentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Parse Error while saving playlistId", e);
                    }}
            });
        }
    }


    private class setUserDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            userId = spotifyApi.getCurrentUser().getId();
            displayName = spotifyApi.getCurrentUser().getDisplayName();
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }
}