package com.example.meloday20;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.SearchView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.meloday20.fragments.PostFragment;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

public class SearchTracksActivity extends AppCompatActivity {
    private static final String TAG = "SearchTracksActivity";
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private SearchView svSearch;
    private RecyclerView rvResults;
    private SearchAdapter adapter;
    private List<Track> tracks;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tracks);

        svSearch = findViewById(R.id.svSearch);
        rvResults = findViewById(R.id.rvResults);
        tracks = new ArrayList<>();
        adapter = new SearchAdapter(this, tracks);
        linearLayoutManager = new LinearLayoutManager(this);
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(linearLayoutManager);


        // Setup search field
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tracks.clear();
                Map<String, Object> options = new HashMap<>();
//                options.put(SpotifyService.OFFSET, offset);
                options.put(SpotifyService.LIMIT, 20);
                spotify.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
                    @Override
                    public void success(TracksPager tracksPager, Response response) {
                        Log.i(TAG, "Search tracks success");
                        tracks.addAll(tracksPager.tracks.items);
//                        for (Track t: tracks) {
//                            Log.i(TAG, t.name + " by: " + t.artists.get(0).name);
//                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(SpotifyError error) {
                        Log.e(TAG, "Search tracks error");
                    }
                });

                // Reset SearchView
                svSearch.clearFocus();
                svSearch.setQuery("", false);
                svSearch.setIconified(true);
                // Set activity title to search query
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}