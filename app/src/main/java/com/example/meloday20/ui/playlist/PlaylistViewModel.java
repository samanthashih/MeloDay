package com.example.meloday20.ui.playlist;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.service.SpotifyServiceSingleton;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.UserPrivate;
import kaaes.spotify.webapi.android.models.UserPublic;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistViewModel extends AndroidViewModel {
    private static final String TAG = PlaylistViewModel.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private String currUserId = currentUser.getUsername();
    private MutableLiveData<String> _currUserPlaylistId = new MutableLiveData<>();
    public LiveData<String> currUserPlaylistId = _currUserPlaylistId;
    private String currentUsersPlaylistId;

    private MutableLiveData<String> _anyUserPlaylistId = new MutableLiveData<>();
    public LiveData<String> anyUserPlaylistId = _anyUserPlaylistId;

    private MutableLiveData<List<PlaylistTrack>> _playlistTracks = new MutableLiveData<>();
    public LiveData<List<PlaylistTrack>> playlistTracks = _playlistTracks;
    private MutableLiveData<Playlist> _playlistDetails = new MutableLiveData<>();
    public LiveData<Playlist> playlistDetails = _playlistDetails;
    private MutableLiveData<UserPrivate> _userDetails = new MutableLiveData<>();
    public LiveData<UserPrivate> userDetails = _userDetails;
    private Map<String, Object> createPlaylistParams = new HashMap<>();
    private String displayName;


    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        getCurrUserParsePlaylistId();
        getCurrUserDetails();
    }

    public void getCurrUserParsePlaylistId() {
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class); // specify what type of data we want to query - ParsePlaylist.class
        query.whereEqualTo(ParsePlaylist.KEY_USER, currentUser);
        query.include(ParsePlaylist.KEY_PLAYLIST_ID); // include data referred by current user
        try {
            currentUsersPlaylistId = query.find().get(0).getPlaylistId();
            _currUserPlaylistId.setValue(currentUsersPlaylistId);
        } catch (Exception e) {
            e.printStackTrace();
            _currUserPlaylistId.setValue(null);
        }
    }

    public void getAnyUserParsePlaylistId(ParseUser user) {
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class); // specify what type of data we want to query - ParsePlaylist.class
        query.whereEqualTo(ParsePlaylist.KEY_USER, user);
        query.include(ParsePlaylist.KEY_PLAYLIST_ID); // include data referred by current user
        try {
            _anyUserPlaylistId.setValue(query.find().get(0).getPlaylistId());
        } catch (Exception e) {
            e.printStackTrace();
            _anyUserPlaylistId.setValue(null);
        }
    }

    public void getPlaylistTracks(String userId, String playlistId) {
        spotify.getPlaylistTracks(userId, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                _playlistTracks.setValue(playlistTrackPager.items);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting track details.", error);
            }
        });
    }

    public void getPlaylistDetails(String userId, String playlistId) {
        spotify.getPlaylist(userId, playlistId, new Callback<Playlist>() {
                @Override
                public void success(Playlist playlist, Response response) {
                    _playlistDetails.setValue(playlist);
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Could not retrieve playlist from Spotify.", error);
                }
            });
    }

    public void getCurrUserDetails() {
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                displayName = userPrivate.display_name;
                _userDetails.setValue(userPrivate);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    public void createNewPlaylist() {
        createPlaylistParams.put("name", displayName + "'s MeloDay");
        createPlaylistParams.put("description", "Your playlist of the year");
        createPlaylistParams.put("public", true);
        spotify.createPlaylist(currUserId, createPlaylistParams, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, Response response) {
                Log.d(TAG, playlist.id);
                currentUsersPlaylistId = playlist.id;
                savePlaylistIdInParse();
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    private void savePlaylistIdInParse() {
        ParsePlaylist playlist = new ParsePlaylist();
        playlist.setUser(currentUser);
        playlist.setPlaylistId(currentUsersPlaylistId);
        playlist.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving playlist Id", e);
                }
                Log.e(TAG, "Saved playlist Id in Parse", e);
            }
        });
    }
}
