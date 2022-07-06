package com.example.meloday20.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();
    private MutableLiveData<List<Post>> _posts = new MutableLiveData<>();
    LiveData<List<Post>> posts = _posts;

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

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
                    _posts.setValue(null);
                    return;
                }
                _posts.setValue(queryPosts);
            }
        });
    }

}