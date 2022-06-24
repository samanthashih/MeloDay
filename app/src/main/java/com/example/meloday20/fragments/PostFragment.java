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
import android.widget.EditText;

import com.example.meloday20.MainActivity;
import com.example.meloday20.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
//import spotify.api.enums.QueryType;
//import spotify.api.spotify.SpotifyApi;
//import spotify.models.search.SearchQueryResult;
//import spotify.models.tracks.TrackFull;

public class PostFragment extends Fragment {
    private final static String TAG = "PostFragment";
//    SpotifyApi spotifyApi = MainActivity.spotifyApi;
    private EditText etSearch;
    Map<String, String> searchItemParams = new HashMap<>();

    public PostFragment() {
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
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = view.findViewById(R.id.etSearch);

    }

//    private class searchTracks extends AsyncTask<String, Void, List<String>> {
//        @Override
//        protected List<String> doInBackground(String... params) {
//            String trackQuery = params[0];
//            String query = "track:" + trackQuery + "artist:" + trackQuery;
//            List<QueryType> queryTypes = new ArrayList<QueryType>();
//            queryTypes.add(QueryType.TRACK);
//            searchItemParams.put("limit", "5");
//            searchItemParams.put("offset", params[1]);
//            SearchQueryResult result = spotifyApi.searchItem(query, queryTypes, searchItemParams);
//            List<TrackFull> tracks = result.getTracks().getItems();
//            List<String> trackIds = new ArrayList<>();
//            for (TrackFull track: tracks) {
//                trackIds.add(track.getId());
//            }
//            return trackIds;
//        }
//        @Override
//        protected void onPostExecute(List<String> result) {
//            super.onPostExecute(result);
//            for (String trackId: result) {
//                Log.i(TAG, trackId);
//            }
//        }
//    }

}