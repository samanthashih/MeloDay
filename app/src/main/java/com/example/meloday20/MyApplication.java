package com.example.meloday20;
import android.app.Application;

import com.example.meloday20.models.Comment;
import com.example.meloday20.models.Like;
import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParsePlaylist.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Like.class);
        ParseObject.registerSubclass(Comment.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
