package com.example.meloday20;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Playlist")
public class ParsePlaylist extends ParseObject {
    public static final String KEY_PLAYLIST_ID = "playlistId";
    public static final String KEY_USER = "user";

    public String getPlaylistId() {
        return getString(KEY_PLAYLIST_ID);
    }
    public void setPlaylistId(String playlistId) {
        put(KEY_PLAYLIST_ID, playlistId);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
