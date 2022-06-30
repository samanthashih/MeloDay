package com.example.meloday20.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meloday20.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();
    MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    public void queryPosts(int skip) {
        Log.i(TAG, "query posts");
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class); // specify what type of data we want to query - Post.class on parstagram database
        query.include(Post.KEY_USER); // include data referred by current user
        query.include(Post.KEY_TRACK_ID); // include data referred by current user
        query.setLimit(20); // only want last 20 photos
        query.addDescendingOrder("createdAt"); // get the newer photos first so sort by createdAt column
        query.setSkip(skip);

        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> queryPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts.", e);
                    posts.setValue(null);
                    return;
                }
                posts.setValue(queryPosts);
            }
        });
    }

}
