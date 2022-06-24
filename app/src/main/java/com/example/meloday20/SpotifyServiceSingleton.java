package com.example.meloday20;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceSingleton {
    private static SpotifyService spotify = null;

    private SpotifyServiceSingleton() {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(SpotifyLoginActivity.accessToken);
        spotify = api.getService();
    }

    public synchronized static SpotifyService getInstance() {
        if (spotify == null) {
            new SpotifyServiceSingleton();
        }
        return spotify;
    }
}