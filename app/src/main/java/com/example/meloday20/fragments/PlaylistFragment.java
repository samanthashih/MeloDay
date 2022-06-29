package com.example.meloday20.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meloday20.adapters.PlaylistAdapter;
import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.R;
import com.example.meloday20.SpotifyServiceSingleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import spotify.api.enums.QueryType;
//import spotify.api.spotify.SpotifyApi;
//import spotify.models.playlists.PlaylistSimplified;
//import spotify.models.playlists.requests.CreateUpdatePlaylistRequestBody;
//import spotify.models.search.SearchQueryResult;
//import spotify.models.tracks.TrackFull;

public class PlaylistFragment extends Fragment {
    private static final String TAG = "PlaylistFragment";
    private ParseUser currentUser;
    private ParsePlaylist usersPlaylist;
    private String userId;
    private String displayName;
    private Button btnCreatePlaylist;
    private RecyclerView rvPlaylistTracks;
    private String playlistId;
    private Map<String, Object> createPlaylistParams = new HashMap<>();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private boolean hasPlaylist;
    private List<PlaylistTrack> playlistTracks;
    private PlaylistAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    public PlaylistFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        getParsePlaylist();
        if (usersPlaylist != null) {
            hasPlaylist = true;
        } else {
            hasPlaylist = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (hasPlaylist) {
            return inflater.inflate(R.layout.fragment_has_playlist, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_playlist, container, false);
        }
    }

    private void getParsePlaylist() {
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class); // specify what type of data we want to query - ParsePlaylist.class
        query.whereEqualTo(ParsePlaylist.KEY_USER, currentUser);
        query.include(ParsePlaylist.KEY_PLAYLIST_ID); // include data referred by current user
        try {
            usersPlaylist = query.find().get(0);
            playlistId = usersPlaylist.getPlaylistId();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        if (hasPlaylist) {
            rvPlaylistTracks = view.findViewById(R.id.rvPlaylistTracks);
            playlistTracks = new ArrayList<>();
            adapter = new PlaylistAdapter(getContext(), playlistTracks);
            linearLayoutManager = new LinearLayoutManager(getContext());
            rvPlaylistTracks.setAdapter(adapter);
            rvPlaylistTracks.setLayoutManager(linearLayoutManager);
            getPlaylistTracks();
        } else {
            btnCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);
            btnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewPlaylist();
                }
            });
        }
    }

    private void getPlaylistTracks() {
        spotify.getPlaylistTracks(userId, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                playlistTracks.addAll(playlistTrackPager.items);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting track details.", error);
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
        playlist.setUser(currentUser);
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