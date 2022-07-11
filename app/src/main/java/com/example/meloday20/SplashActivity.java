package com.example.meloday20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.meloday20.login.SpotifyLoginActivity;
import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.parse.ParseUser;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfLoggedIn();
            }
        },3000);
    }

    private void routeToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
    private void routeToLoginActivity() {
        Intent intent = new Intent(this, SpotifyLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void checkIfLoggedIn() {
        try {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String accessToken = currentUser.getString("accessToken");
            SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
            spotify.getMe(new Callback<UserPrivate>() {
                @Override
                public void success(UserPrivate userPrivate, Response response) {
                    Log.e(TAG, "Already logged in -  go to main");
                    routeToMainActivity();
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Need to login to Spotify - go to login");
                    routeToLoginActivity();
                }
            });
        }
        catch(Exception e) {
            Log.e(TAG, "Need to login to Parse - go to login");
            routeToLoginActivity();
        }
    }

}