package com.example.meloday20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

import spotify.api.spotify.SpotifyApi;

public class SpotifyLoginActivity extends AppCompatActivity {
    private static final String TAG = "SpotifyLoginActivity";
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "f739c53578ef4b98b5ec6e8068bc4ec6";
    private static final String CLIENT_SECRET = "16e6e2b17da84d3d9ebabac507a1a537";
    private final String REDIRECT_URI = "com.example.meloday20://callback";
    SpotifyApi spotifyApi;
    String accessToken;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);
    }

    public void onLoginClick(View view) {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

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
                    // Handle successful response
                    accessToken = response.getAccessToken();
                    spotifyApi = new SpotifyApi(accessToken);
                    new getUserDetails().execute();
                    Intent toMain = new Intent(this, MainActivity.class);
                    toMain.putExtra("accessToken", accessToken);
                    startActivity(toMain);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }

    private class getUserDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            username = spotifyApi.getCurrentUser().getId();
            loginUser(username, "password");
           // Log.e(TAG, username);
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    private void loginUser(String username, String password) {
        Log.e(TAG, "Login attempt for user: " + username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!= null) {
                    signUpUser(username, password);
                    return;
                }
            }
        });
    }

    private void signUpUser(String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "signed up user: "+username);
                return;
            }
        });
    }

}