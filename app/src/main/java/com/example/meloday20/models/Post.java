package com.example.meloday20.models;

import com.example.meloday20.SpotifyServiceSingleton;
import com.example.meloday20.utils.GetDetails;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

import kaaes.spotify.webapi.android.SpotifyService;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String TAG = Post.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    public static final String KEY_USER = "user";
    public static final String KEY_TRACK_ID = "trackId";
    public static final String KEY_CREATED_AT = "createdAt";

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

    public String getCreatedAtDate() {
        Date date = this.getCreatedAt();
        return GetDetails.getDateString(date);
    }

    public String getUsername() {
        ParseUser postUser = getUser();
        return postUser.getUsername();
    }
}
