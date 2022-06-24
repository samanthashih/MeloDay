package com.example.meloday20.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meloday20.MainActivity;
import com.example.meloday20.R;
import com.example.meloday20.SpotifyLoginActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import spotify.api.spotify.SpotifyApi;
import spotify.models.playlists.requests.CreateUpdatePlaylistRequestBody;

public class PlaylistFragment extends Fragment {
    private String accessToken;
    private SpotifyApi spotifyApi;
    private String userId;
    private String displayName;
    private Button btnCreatePlaylist;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accessToken = getArguments().getString("accessToken");
        }
        spotifyApi = new SpotifyApi(accessToken);
        new getUserDetails().execute();
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

//        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new createPlaylist().execute();
//            }
//        });
    }


    private class getUserDetails extends AsyncTask<String, Void, String> {
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