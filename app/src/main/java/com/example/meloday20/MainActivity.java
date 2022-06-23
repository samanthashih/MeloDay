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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static String accessToken;

    private BottomNavigationView bottomNavigationView;
    final FragmentManager fts = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        accessToken = getIntent().getStringExtra("accessToken");
        Log.e(TAG, accessToken);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_post:
                        fragment = new PostFragment();
                        break;
                    case R.id.action_playlist:
                        fragment = new PlaylistFragment();
                        break;
                    case R.id.action_home:
                    default:
                        fragment = new HomeFragment();
                        break;
                }
                fts.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }




}