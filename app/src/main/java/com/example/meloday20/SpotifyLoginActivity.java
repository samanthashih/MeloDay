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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyLoginActivity extends AppCompatActivity {
    private static final String TAG = "SpotifyLoginActivity";
    private static final int REQUEST_CODE = 1337;
    private static final String CLIENT_ID = "f739c53578ef4b98b5ec6e8068bc4ec6";
    private static final String CLIENT_SECRET = "16e6e2b17da84d3d9ebabac507a1a537";
    private final String REDIRECT_URI = "com.example.meloday20://callback";
    private SpotifyApi spotifyApi;
    private String accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_login);
//        Log.e(TAG, REDIRECT_URI);
    }

    public void onLoginClick(View view) {
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"streaming"});
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
                    SpotifyApi api = new SpotifyApi();
                    api.setAccessToken(accessToken);
                    SpotifyService spotify = api.getService();

                    spotify.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", new Callback<Album>() {
                        @Override
                        public void success(Album album, Response response) {
                            Log.d("Album success", album.name);
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("Album failure", error.toString());
                        }
                    });
//                    new createParseUser().execute();
//                    Intent toMain = new Intent(this, MainActivity.class);
//                    toMain.putExtra("accessToken", accessToken);
//                    startActivity(toMain);
                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response==
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


//    private class createParseUser extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... params) {
//            ParseUser user = new ParseUser();
//            String username = spotifyApi.getCurrentUser().getId();
//            String password = accessToken;
//            user.setUsername(username);
//            user.setPassword(password);
//            user.signUpInBackground(new SignUpCallback() {
//                @Override
//                public void done(ParseException e) {
//                    Intent intent = new Intent(SpotifyLoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            });
//            return null;
//        }
//        @Override
//        protected void onPostExecute(String result)
//        {
//            super.onPostExecute(result);
//        }
//    }

}