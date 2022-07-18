package com.example.meloday20.models;

import com.example.meloday20.utils.GetDetails;
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

    public void setPost(Post post) {put(KEY_POST, post);}

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }
    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

    public String getCreatedAtDate() {
        return GetDetails.getDateString(this.getCreatedAt());
    }
}
