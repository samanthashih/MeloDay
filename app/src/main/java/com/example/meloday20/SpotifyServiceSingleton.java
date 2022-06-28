package com.example.meloday20;

import com.parse.ParseUser;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceSingleton {
    private static SpotifyService spotify = null;
    private static String accessToken = SpotifyLoginActivity.accessToken;

    private SpotifyServiceSingleton() {
        SpotifyApi api = new SpotifyApi();
//        accessToken = ParseUser.getCurrentUser().getString("accessToken");
        api.setAccessToken(accessToken);
        spotify = api.getService();
    }

    public synchronized static SpotifyService getInstance() {
        if (spotify == null) {
            new SpotifyServiceSingleton();
        }
        return spotify;
    }
}