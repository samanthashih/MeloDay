package com.example.meloday20.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.meloday20.MainActivity;
import com.example.meloday20.R;
import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//import spotify.api.spotify.SpotifyApi;

public class SpotifyLoginActivity extends AppCompatActivity {
    private static final String TAG = "SpotifyLoginActivity";
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "f739c53578ef4b98b5ec6e8068bc4ec6";
    private static String username;
    private final String REDIRECT_URI = "com.example.meloday20://callback";
    public static SpotifyService spotify;
    public String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);
    }

    public void onLoginClick(View view) {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setShowDialog(true);
        builder.setScopes(new String[]{"streaming", "ugc-image-upload", "playlist-read-collaborative", "playlist-modify-public" , "playlist-read-private" , "playlist-modify-private", "user-read-email",
                "user-read-private", "user-library-modify", "user-library-read", "user-read-recently-played", "user-read-playback-position", "user-top-read" });
        AuthorizationRequest request = builder.build();

        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Handle successful response -- set up network client
                    accessToken = response.getAccessToken();
                    spotify = SpotifyServiceSingleton.getInstance(accessToken);
                    Log.i(TAG, accessToken);
                    spotify.getMe(new Callback<UserPrivate>() {
                        @Override
                        public void success(UserPrivate userPrivate, Response response) {
                            username = userPrivate.id;
                            loginUser(username, "password", accessToken);
                            Intent toMain = new Intent(SpotifyLoginActivity.this, MainActivity.class);
                            startActivity(toMain);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            Log.d(TAG, error.toString());
                        }
                    });
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.e(TAG, "Spotify auth error");
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private void setParseUserAccessToken(String accessToken) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("accessToken", accessToken);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving accessToken", e);
                }
                Log.i(TAG, "Logged in: " + currentUser.getUsername());
            }
        });
    }

    private void setParseUserPfp(String pfpUrl) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profilePicUrl", pfpUrl);
        currentUser.put("alarmTime", "11:00 PM");
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving pfp", e);
                }
            }
        });
    }

    private void loginUser(String username, String password, String accessToken) {
        Log.i(TAG, "Login attempt for user: " + username);
        try {
            ParseUser.logIn(username, password);
        } catch (ParseException e) {
            signUpUser(username, password, accessToken);
            return;
        }
        setParseUserAccessToken(accessToken);
    }

    private void signUpUser(String username, String password, String accessToken) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        setParseUserAccessToken(accessToken);
                        if (userPrivate.images.size() > 0) {
                            setParseUserPfp(userPrivate.images.get(0).url);
                        }
                        Log.i(TAG, "signed up user: " + username);
                        return;
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "");
            }
        });
    }

}