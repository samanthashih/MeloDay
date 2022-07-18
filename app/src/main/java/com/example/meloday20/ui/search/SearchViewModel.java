package com.example.meloday20.ui.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.service.SpotifyServiceSingleton;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = SearchViewModel.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private static Map<String, Object> options = new HashMap<>();
    private MutableLiveData<List<Track>> _tracks = new MutableLiveData<>();
    LiveData<List<Track>> tracks = _tracks;

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void getSearchTracks(String query) {
        options.put(SpotifyService.LIMIT, 20);
        spotify.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
            @Override
            public void success(TracksPager tracksPager, Response response) {
                Log.i(TAG, "Search tracks success");
                _tracks.setValue(tracksPager.tracks.items);
            }
            @Override
            public void failure(SpotifyError error) {
                Log.e(TAG, "Search tracks error");
            }
        });
    }

}
