package com.example.meloday20.home;

import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.example.meloday20.utils.GetDetails;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import kaaes.spotify.webapi.android.SpotifyService;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String TAG = Post.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_TRACK_ID = "trackId";
    public static final String KEY_TRACK_NAME = "trackName";
    public static final String KEY_TRACK_ARTISTS = "trackArtists";
    public static final String KEY_TRACK_IMAGE_URL = "trackImageUrl";

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

    public String getTrackName() {
        return getString(KEY_TRACK_NAME);
    }
    public void setTrackName(String trackName) {
        put(KEY_TRACK_NAME, trackName);
    }

    public String getTrackArtists() {
        return getString(KEY_TRACK_ARTISTS);
    }
    public void setTrackArtists(String trackArtists) {
        put(KEY_TRACK_ARTISTS, trackArtists);
    }

    public String getTrackImageUrl() {
        return getString(KEY_TRACK_IMAGE_URL);
    }
    public void setTrackImageUrl(String trackImageUrl) {
        put(KEY_TRACK_IMAGE_URL, trackImageUrl);
    }

    public String getCreatedAtDate() {
        return GetDetails.getDateString(this.getCreatedAt());
    }

    public String getUsername() {
        return getUser().getUsername();
    }
}
