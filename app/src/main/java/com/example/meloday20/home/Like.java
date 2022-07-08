package com.example.meloday20.home;

import android.util.Log;

import com.example.meloday20.playlist.ParsePlaylist;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Like")
public class Like extends ParseObject {
    private static final String TAG = Post.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

//    public Post getPostId() {
//        return getParseObject(KEY_POST);
//    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

//    public boolean addedLike() {
//        boolean returnValue = true;
//        Log.i(TAG, "adding like!");
//
//        return returnValue;
//    }

}
