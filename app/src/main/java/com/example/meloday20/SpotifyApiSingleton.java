package com.example.meloday20;

import spotify.api.spotify.SpotifyApi;

public class SpotifyApiSingleton {

    public static SpotifyApi spotifyApi;
    private SpotifyApiSingleton() {
        // private constructor so that class cannot be instantiated from outside

//        spotifyApi = new SpotifyApi();
    }
}
