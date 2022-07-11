package com.example.meloday20.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.R;
import com.example.meloday20.playlist.PlaylistViewModel;
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
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.client.Response;

public class SearchFragment extends Fragment {
    private final static String TAG = SearchFragment.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private SearchViewModel viewModel;
    private SearchView svSearch;
    private RecyclerView rvResults;
    private LottieAnimationView lottieMusicGuy;
    private TextView tvIfYouListen;
    private TextView tvReally;
    private SearchAdapter adapter;
    private List<Track> results;
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        initViews(view);
        setUpSearch();
    }

    private void setUpSearch() {
        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                displaySearchResults(query);
                // Reset SearchView
                svSearch.clearFocus();
                svSearch.setQuery("", false);
                svSearch.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initViews(@NonNull View view) {
        svSearch = view.findViewById(R.id.svSearch);
        rvResults = view.findViewById(R.id.rvResults);
        lottieMusicGuy = view.findViewById(R.id.lottieMusicGuy);
        tvIfYouListen = view.findViewById(R.id.tvIfYouListen);
        tvReally = view.findViewById(R.id.tvReally);
        results = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), results);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(linearLayoutManager);
    }

    private void displaySearchResults(String query) {
        lottieMusicGuy.setVisibility(View.GONE);
        tvIfYouListen.setVisibility(View.GONE);
        tvReally.setVisibility(View.GONE);
        viewModel.getSearchTracks(query);
        viewModel.tracks.observe(getViewLifecycleOwner(), new Observer<List<Track>>() {
            @Override
            public void onChanged(List<Track> newTracks) {
                results.clear();
                results.addAll(newTracks);
                adapter.notifyDataSetChanged();
            }
        });
    }
}