//package com.example.meloday20.fragments;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.widget.SearchView;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.meloday20.R;
//import com.example.meloday20.SearchAdapter;
//import com.example.meloday20.SpotifyServiceSingleton;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import kaaes.spotify.webapi.android.SpotifyCallback;
//import kaaes.spotify.webapi.android.SpotifyError;
//import kaaes.spotify.webapi.android.SpotifyService;
//import kaaes.spotify.webapi.android.models.Track;
//import kaaes.spotify.webapi.android.models.TracksPager;
//import retrofit.client.Response;
//
//public class SearchFragment extends Fragment {
//    private static final String TAG = "SearchFragment";
//    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance();
//    private SearchView svSearch;
//    private RecyclerView rvResults;
//    private SearchAdapter adapter;
//    private List<Track> tracks;
//    LinearLayoutManager linearLayoutManager;
//
//    public SearchFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_search, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        svSearch = view.findViewById(R.id.svSearch);
//        rvResults = view.findViewById(R.id.rvResults);
//        tracks = new ArrayList<>();
//        adapter = new SearchAdapter(getContext(), tracks);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//        rvResults.setAdapter(adapter);
//        rvResults.setLayoutManager(linearLayoutManager);
//
//
//
//        // Setup search field
//        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Map<String, Object> options = new HashMap<>();
////                options.put(SpotifyService.OFFSET, offset);
//                options.put(SpotifyService.LIMIT, 5);
//                spotify.searchTracks(query, options, new SpotifyCallback<TracksPager>() {
//                    @Override
//                    public void success(TracksPager tracksPager, Response response) {
//                        Log.e(TAG, "Search tracks success");
//                        tracks = tracksPager.tracks.items;
//                        for (Track t: tracks) {
//                            Log.e(TAG, t.name);
//                        }
//                    }
//
//                    @Override
//                    public void failure(SpotifyError error) {
//                        Log.e(TAG, "Search tracks error");
//                    }
//                });
//
//                // Reset SearchView
//                svSearch.clearFocus();
//                svSearch.setQuery("", false);
//                svSearch.setIconified(true);
//                // Set activity title to search query
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//    }
//
//}