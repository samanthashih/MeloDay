//package com.example.meloday20;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//
//import com.spotify.android.appremote.api.ConnectionParams;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//import com.spotify.android.appremote.api.Connector;
//import com.spotify.android.appremote.api.SpotifyAppRemote;
//
//import com.spotify.protocol.client.Subscription;
//import com.spotify.protocol.types.PlayerState;
//
//public class SpotifyPlayer extends AppCompatActivity {
//    private static final String TAG = SpotifyPlayer.class.getSimpleName();
//    private static final String CLIENT_ID = "f739c53578ef4b98b5ec6e8068bc4ec6";
//    private static final String REDIRECT_URI = "com.example.meloday20://callback";
//    private SpotifyAppRemote mSpotifyAppRemote;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_spotify_player);
//
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // We will start writing our code here.
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    @Override
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d(TAG, "Connected! Yay!");
//                        // Now you can start interacting with App Remote
////                        connected();
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        Log.e(TAG, throwable.getMessage(), throwable);
//                        // Something went wrong when attempting to connect! Handle errors here
//                    }
//                });
//    }
//
//    private void connected() {
//        // Then we will write some more code here.
//        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//}