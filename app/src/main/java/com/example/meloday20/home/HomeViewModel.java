package com.example.meloday20.home;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.meloday20.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        query.include(Post.KEY_TRACK_NAME);
        query.include(Post.KEY_TRACK_ARTISTS);
        query.include(Post.KEY_TRACK_IMAGE_URL);
        query.include(Post.KEY_CREATED_AT);
        query.addDescendingOrder(Post.KEY_CREATED_AT); // get the newer photos first so sort by createdAt column
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
