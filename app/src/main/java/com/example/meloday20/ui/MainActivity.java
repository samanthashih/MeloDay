package com.example.meloday20.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.example.meloday20.R;
import com.example.meloday20.ui.home.HomeFragment;
import com.example.meloday20.ui.playlist.PlaylistFragment;
import com.example.meloday20.ui.search.SearchFragment;
import com.example.meloday20.ui.profile.ProfileFragment;
import com.example.meloday20.service.SpotifyServiceSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import kaaes.spotify.webapi.android.SpotifyService;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String accessToken;
    public  SpotifyService spotify;
    public static BottomNavigationView bottomNavigationView;
    final FragmentManager fts = getSupportFragmentManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accessToken = ParseUser.getCurrentUser().getString(MainActivity.this.getString(R.string.keyAccessToken));
        spotify  = SpotifyServiceSingleton.getInstance(accessToken);
        setContentView(R.layout.activity_main);
        initBottomNav();
        if (getIntent().getStringExtra("shareToInstaStory") != null) {
            try {
                shareToInstagram(getIntent().getStringExtra("shareToInstaStory"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shareToInstagram(String imageUrl) throws IOException, URISyntaxException {
        Log.i(TAG, "share to instagram story image url: " + imageUrl);
//        URL url = new URL(imageUrl);
//        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(), image, "Title", null);
//        Uri uriCover = Uri.parse(path);

//        Uri uriCover = Uri.parse(imageUrl);
//        Log.i(TAG, "cover uri: " + uriCover.toString());

        Resources resources = this.getResources();
        Uri uri = (new Uri.Builder())
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.default_playlist_cover))
                .appendPath(resources.getResourceTypeName(R.drawable.default_playlist_cover))
                .appendPath(resources.getResourceEntryName(R.drawable.default_playlist_cover))
                .build();
        Log.i(TAG, "default image: " + uri.toString());


        Intent feedIntent = new Intent(Intent.ACTION_SEND);
        feedIntent.setType("image/*");
        feedIntent.putExtra(Intent.EXTRA_STREAM, uri);
        feedIntent.setPackage("com.instagram.android");

        Intent storiesIntent = new Intent("com.instagram.share.ADD_TO_STORY");
        storiesIntent.setDataAndType(uri, "image/*");
        storiesIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        storiesIntent.setPackage("com.instagram.android");
        storiesIntent.putExtra("interactive_asset_uri", uri);
        storiesIntent.putExtra("top_background_color", "#33FF33");
        storiesIntent.putExtra("bottom_background_color", "#FF00FF");

        this.grantUriPermission(
                "com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        feedIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{storiesIntent});
        startActivity(feedIntent);
    }

    private void initBottomNav() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_playlist:
                        fragment = new PlaylistFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
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