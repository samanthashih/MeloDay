package com.example.meloday20.fragments;

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

import com.example.meloday20.models.Post;
import com.example.meloday20.adapters.PostAdapter;
import com.example.meloday20.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    private RecyclerView rvPosts;
    private PostAdapter adapter;
    private List<Post> posts;
    LinearLayoutManager linearLayoutManager;

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
        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), posts);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);
        queryPosts();
    }

    private void queryPosts() {
        Log.i(TAG, "query posts");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class); // specify what type of data we want to query - Post.class on parstagram database
        query.include(Post.KEY_USER); // include data referred by current user
        query.include(Post.KEY_TRACK_ID); // include data referred by current user
        query.setLimit(20); // only want last 20 photos
        query.addDescendingOrder("createdAt"); // get the newer photos first so sort by createdAt column
        query.setSkip(posts.size());

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts.", e);
                    return;
                }
                // no error
                // save received posts to list and notify adapter of new data
                posts.addAll(queryPosts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}