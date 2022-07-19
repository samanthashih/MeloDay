package com.example.meloday20.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.meloday20.R;
import com.example.meloday20.models.Post;
import com.example.meloday20.ui.playlist.PlaylistAdapter;
import com.example.meloday20.ui.playlist.PlaylistViewModel;
import com.example.meloday20.utils.CommonActions;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<Post> homePosts;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar progressBar;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        displayPosts();
        // Request permissions for RECORD_AUDIO and MODIFY_AUDIO_SETTINGS needed to animate audio waveform
        CommonActions.requestPermissions(this);
    }
    private void init(@NonNull View view) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        homePosts = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), homePosts);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(postAdapter);
        rvPosts.setLayoutManager(linearLayoutManager);
    }

    private void displayPosts() {
        homeViewModel.queryPosts(0);
        Observer<List<Post>> postObserver = new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                homePosts.addAll(posts);
                postAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        };
        homeViewModel.posts.observe(getViewLifecycleOwner(),postObserver);
    }

}