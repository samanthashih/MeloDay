package com.example.meloday20;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class SpotifyServiceSingleton {
    public static SpotifyService spotify;
    private SpotifyServiceSingleton() {
        // private constructor so that class cannot be instantiated from outside
//        spotify.;
    }
}