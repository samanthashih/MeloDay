package com.example.meloday20.utils;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceSingleton {
    private static SpotifyService spotify = null;

    private SpotifyServiceSingleton(String accessToken) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(accessToken);
        spotify = api.getService();
    }

    public synchronized static SpotifyService getInstance(String accessToken) {
        if (spotify == null) {
            new SpotifyServiceSingleton(accessToken);
        }
        return spotify;
    }
}