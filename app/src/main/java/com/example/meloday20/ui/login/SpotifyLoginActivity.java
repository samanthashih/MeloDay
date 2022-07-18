package com.example.meloday20.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.meloday20.ui.MainActivity;
import com.example.meloday20.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

//import spotify.api.spotify.SpotifyApi;

public class SpotifyLoginActivity extends AppCompatActivity {
    private static final String TAG = SpotifyLoginActivity.class.getSimpleName();
    private static final int REQUEST_CODE = 1337;
    private String CLIENT_ID;
    private String REDIRECT_URI;
    private SpotifyLoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);
        init();
    }

    private void init() {
        CLIENT_ID = getString(R.string.client_id);
        REDIRECT_URI = getString(R.string.redirect_uri);
        viewModel = new ViewModelProvider(this).get(SpotifyLoginViewModel.class);
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
                // Response was successful and contains access token
                case TOKEN:
                    String accessToken = response.getAccessToken();
                    viewModel.loginUser(accessToken);
                    Observer<Boolean> loginUserObserver = new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean postedToday) {
                            Intent toMain = new Intent(SpotifyLoginActivity.this, MainActivity.class);
                            startActivity(toMain);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    };
                    viewModel.loginUser.observe(SpotifyLoginActivity.this, loginUserObserver);
                    break;
                case ERROR:
                    Log.e(TAG, "Spotify authentication error");
                    break;
                default:
                    Log.e(TAG, "Spotify authentication error, most likely auth flow canceled");
            }
        }
    }

}