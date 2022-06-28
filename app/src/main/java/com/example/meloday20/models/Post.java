package com.example.meloday20.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.meloday20.SpotifyServiceSingleton;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.apache.commons.lang3.StringUtils;

import java.time.Month;
import java.util.Calendar;
import java.util.Date;

import kaaes.spotify.webapi.android.SpotifyService;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String TAG = Post.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    public static final String KEY_USER = "user";
    public static final String KEY_TRACK_ID = "trackId";
    public static final String KEY_CREATED_AT = "createdAt";

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

    public String getCreatedAtDate() {
        Date date = this.getCreatedAt();
        String dateString = calculateTimeAgo(date);
        return dateString;
    }

    public String getUsername() {
        ParseUser postUser = getUser();
        return postUser.getUsername();
    }

    @NonNull
    public static String calculateTimeAgo(Date createdAt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdAt);

        try {
            int monthNum = calendar.get(Calendar.MONTH)+1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int year = calendar.get(Calendar.YEAR);
            String month = StringUtils.capitalize(Month.of(monthNum).name().toLowerCase());
            return month + " " + String.valueOf(day) + ", " + String.valueOf(year);
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }
        return "";
    }
}
