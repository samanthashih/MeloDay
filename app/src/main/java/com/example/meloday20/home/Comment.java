package com.example.meloday20.home;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    private static final String TAG = Comment.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_MESSAGE = "message";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setPost(Post post) {
        put(KEY_POST, post);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

}
