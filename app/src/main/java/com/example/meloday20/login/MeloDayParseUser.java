package com.example.meloday20.login;

import com.parse.ParseUser;

public class MeloDayParseUser extends ParseUser {
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_PROFILE_PIC_URL = "profilePicUrl";

    public String getProfilePicUrl() {
        return getString(KEY_PROFILE_PIC_URL);
    }
    public void setProfilePicUrl(String profilePicUrl) {
        put(KEY_PROFILE_PIC_URL, profilePicUrl);
    }
}
