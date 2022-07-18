package com.example.meloday20.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.home.post.Post;
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
        // Specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // Include data
        query.include(Post.KEY_USER);
        query.include(Post.KEY_TRACK_ID);
        query.include(Post.KEY_TRACK_NAME);
        query.include(Post.KEY_TRACK_ARTISTS);
        query.include(Post.KEY_TRACK_IMAGE_URL);
        query.include(Post.KEY_CREATED_AT);
        // Get the newer photos first - sort by createdAt column
        query.addDescendingOrder(Post.KEY_CREATED_AT);
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
