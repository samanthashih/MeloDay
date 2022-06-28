package com.example.meloday20.fragments;

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
import android.widget.EditText;

import com.example.meloday20.MainActivity;
import com.example.meloday20.ParsePlaylist;
import com.example.meloday20.R;
import com.example.meloday20.SpotifyLoginActivity;
import com.example.meloday20.SpotifyServiceSingleton;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Body;

//import spotify.api.enums.QueryType;
//import spotify.api.spotify.SpotifyApi;
//import spotify.models.playlists.PlaylistSimplified;
//import spotify.models.playlists.requests.CreateUpdatePlaylistRequestBody;
//import spotify.models.search.SearchQueryResult;
//import spotify.models.tracks.TrackFull;

public class PlaylistFragment extends Fragment {
    private static final String TAG = "PlaylistFragment";
    private String userId;
    private String displayName;
    private Button btnCreatePlaylist;
    private String playlistId;
    private Map<String, Object> createPlaylistParams = new HashMap<>();
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance();


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
        btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
        userId = ParseUser.getCurrentUser().getUsername();
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                displayName = userPrivate.display_name;
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });

        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPlaylist();
            }
        });
    }

    private void createNewPlaylist() {
        createPlaylistParams.put("name", displayName + "'s MeloDay");
        createPlaylistParams.put("description", "Your playlist of the year");
        createPlaylistParams.put("public", true);

        spotify.createPlaylist(userId, createPlaylistParams, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.d(TAG, playlist.id);
                playlistId = playlist.id;
                savePlaylistIdParse();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    private void savePlaylistIdParse() {
        ParsePlaylist playlist = new ParsePlaylist();
        playlist.setUser(ParseUser.getCurrentUser());
        playlist.setPlaylistId(playlistId);
        playlist.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving playlistId", e);
                }}
        });
    }
}