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
        btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId = ParseUser.getCurrentUser().getUsername();
                createNewPlaylist();
            }
        });
    }

    private void createNewPlaylist() {
        createPlaylistParams.put("name", "new meloday save playlist id parse");
        createPlaylistParams.put("description", "wow");
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
        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        ParseObject playlist = new ParseObject("ParsePlaylist");
        playlist.put("user", ParseObject.createWithoutData("_User", currentUserId));
        playlist.put("playlistId", playlistId);
        playlist.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving playlistId", e);
                }}
        });
    }


    // Spotify API Tasks
//    private class createPlaylist extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            setNewPlaylistId();
//            parseSavePlaylistId();
//            return null;
//        }
//
//        private void setNewPlaylistId() {
//            CreateUpdatePlaylistRequestBody body = new CreateUpdatePlaylistRequestBody(displayName + "'s meloday", "my description", true, false);
//            spotifyApi.createPlaylist(userId, body);
//            getPlaylistsParams.put("limit", "1");
//            PlaylistSimplified playlist =  spotifyApi.getPlaylists(getPlaylistsParams).getItems().get(0);
//            playlistId = playlist.getId();
//        }
//
//        private void parseSavePlaylistId() {
//            ParseUser currentUser = ParseUser.getCurrentUser();
//            currentUser.put("playlistId", playlistId);
//            currentUser.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e != null) {
//                        Log.e(TAG, "Parse Error while saving playlistId", e);
//                    }}
//            });
//        }
//    }
//
//
//    private class setUserDetails extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            userId = spotifyApi.getCurrentUser().getId();
//            displayName = spotifyApi.getCurrentUser().getDisplayName();
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result)
//        {
//            super.onPostExecute(result);
//        }
//    }
}