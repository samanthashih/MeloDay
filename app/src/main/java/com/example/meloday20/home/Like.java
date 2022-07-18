package com.example.meloday20.home;

import com.example.meloday20.home.post.Post;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Like")
public class Like extends ParseObject {
    private static final String TAG = Like.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }
}
