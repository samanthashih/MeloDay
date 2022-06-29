package com.example.meloday20.utils;

import com.bumptech.glide.Glide;

import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;

public class GetDetails {
    public static String getArtistsString(List<ArtistSimple> artists) {
        String artistsString = artists.get(0).name;
        if (artists.size() > 1) {
            for (int i = 1; i < artists.size(); i++) {
                artistsString = artistsString + ", " + artists.get(i).name;
            }
        }
        return artistsString;
    }
}
