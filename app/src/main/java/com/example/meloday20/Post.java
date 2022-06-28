package com.example.meloday20;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_TRACK_ID = "trackId";


    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getTrackId() {
        return getString(KEY_TRACK_ID);
    }
    public void setTrackId(String trackId) {
        put(KEY_TRACK_ID, trackId);
    }
}
