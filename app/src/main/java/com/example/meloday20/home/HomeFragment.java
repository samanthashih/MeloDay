package com.example.meloday20.home;

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
import android.widget.ProgressBar;

import com.example.meloday20.R;
import com.example.meloday20.home.post.Post;
import com.example.meloday20.home.post.PostAdapter;
import com.example.meloday20.utils.CommonActions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel viewModel;
    private RecyclerView rvPosts;
    private PostAdapter adapter;
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

    private void displayPosts() {
        viewModel.queryPosts(0);
        Observer<List<Post>> postObserver = new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                homePosts.addAll(posts);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        };
        viewModel.posts.observe(getViewLifecycleOwner(),postObserver);
    }

    private void init(@NonNull View view) {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        rvPosts = view.findViewById(R.id.rvPosts);
        progressBar = view.findViewById(R.id.progressBar);
        homePosts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), homePosts);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);
    }
}