package com.example.meloday20.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meloday20.R;
import com.example.meloday20.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class SearchFragment extends Fragment {
    private SearchView svSearch;
    private RecyclerView rvResults;
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        svSearch = view.findViewById(R.id.svSearch);
        rvResults = view.findViewById(R.id.rvResults);
        tracks = new ArrayList<>();
        adapter = new SearchAdapter(getContext(), tracks);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvResults.setAdapter(adapter);
        rvResults.setLayoutManager(linearLayoutManager);
    }
}