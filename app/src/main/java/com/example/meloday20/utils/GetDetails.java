package com.example.meloday20.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;
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

    @NonNull
    public static String getDateString(Date createdAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);
        try {
            int monthNum = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);
            String month = StringUtils.capitalize(Month.of(monthNum).name().toLowerCase());
            return month + " " + day + ", " + year;
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }
        return "";
    }
}
