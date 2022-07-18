package com.example.meloday20.service;

import android.util.Log;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceSingleton {
    private static String TAG = SpotifyServiceSingleton.class.getSimpleName();
    private static SpotifyService spotify = null;
    private static String currAccessToken;

    private SpotifyServiceSingleton(String accessToken) {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(accessToken);
        Log.i(TAG, accessToken);
        spotify = api.getService();
        currAccessToken = accessToken;
    }

    public synchronized static SpotifyService getInstance(String accessToken) {
        if (spotify == null || !currAccessToken.equals(accessToken)) {
            new SpotifyServiceSingleton(accessToken);
        }
        return spotify;
    }
}