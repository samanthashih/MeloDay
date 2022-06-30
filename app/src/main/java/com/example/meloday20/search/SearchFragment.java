package com.example.meloday20.search;

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
import android.widget.SearchView;

import com.example.meloday20.R;
import com.example.meloday20.utils.SpotifyServiceSingleton;
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
//
//import spotify.api.enums.QueryType;
//import spotify.api.spotify.SpotifyApi;
//import spotify.models.search.SearchQueryResult;
//import spotify.models.tracks.TrackFull;

public class SearchFragment extends Fragment {
    private final static String TAG = SearchFragment.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private Button btnSearch;
    private Track track;
    private SearchView svSearch2;
    private RecyclerView rvResults2;
    private SearchAdapter adapter;
    private List<Track> tracks;
    LinearLayoutManager linearLayoutManager;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            track = (Track) Parcels.unwrap(getArguments().getParcelable("track"));
            Log.i(TAG, track.name);
        }
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svSearch2 = view.findViewById(R.id.svSearch2);
        rvResults2 = view.findViewById(R.id.rvResults2);
        tracks = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), tracks);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvResults2.setAdapter(adapter);
        rvResults2.setLayoutManager(linearLayoutManager);


        // Setup search field
        svSearch2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(SpotifyError error) {
                        Log.e(TAG, "Search tracks error");
                    }
                });

                // Reset SearchView
                svSearch2.clearFocus();
                svSearch2.setQuery("", false);
                svSearch2.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


}