package com.example.meloday20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.meloday20.fragments.HomeFragment;
import com.example.meloday20.fragments.PlaylistFragment;
import com.example.meloday20.fragments.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

import spotify.api.spotify.SpotifyApi;
import spotify.models.albums.AlbumFull;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static String accessToken;
    SpotifyApi spotifyApi;

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fts = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessToken = getIntent().getStringExtra("accessToken");
        spotifyApi = new SpotifyApi(accessToken);
        new MyTask().execute();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                Bundle bundle = new Bundle();
                bundle.putString("accessToken", accessToken);
                switch (item.getItemId()) {
                    case R.id.action_post:
                        fragment = new PostFragment();
                        fragment.setArguments(bundle);
                        break;
                    case R.id.action_playlist:
                        fragment = new PlaylistFragment();
                        fragment.setArguments(bundle);
                        break;
                    case R.id.action_home:
                    default:
                        fragment = new HomeFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                fts.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
//            Map<String, String> optionalParameters = new HashMap<>();
//            AlbumFull albumFull = spotifyApi.getAlbum("2dIGnmEIy1WZIcZCFSj6i8", optionalParameters);
//            String name = spotifyApi.getCurrentUser().getId();
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }




}