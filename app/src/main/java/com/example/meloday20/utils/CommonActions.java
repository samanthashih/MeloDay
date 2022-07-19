package com.example.meloday20.utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.audiofx.Visualizer;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.meloday20.models.Like;
import com.example.meloday20.models.Post;
import com.example.meloday20.models.PreviewPlayer;
import com.example.meloday20.service.PreviewPlayerSingleton;
import com.example.meloday20.service.SpotifyServiceSingleton;
import com.example.meloday20.service.VisualizerSingleton;
import com.example.meloday20.ui.audioWaveAnimation.WaveFormView;
import com.example.meloday20.ui.home.HomeFragment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommonActions {
    private static String TAG = CommonActions.class.getSimpleName();

    public static void likePost(Post post) throws ParseException {
        Like like = new Like();
        like.setUser(ParseUser.getCurrentUser());
        like.setPost(post);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue saving the post like" , e);
                    return;
                }
                Log.i(TAG, "Like was saved!");
            }
        });
    }

    public static void unLikePost(Post post) throws ParseException {
        post.deleteUserLikeOnPost();
    }


    public static void requestPermissions(Fragment fragment) {
        ActivityResultLauncher<String[]> audioPermissionRequest =
                fragment.registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean recordAudioGranted = result.getOrDefault(
                                    Manifest.permission.RECORD_AUDIO, false);
                            Boolean modifyAudioGranted = result.getOrDefault(
                                    Manifest.permission.MODIFY_AUDIO_SETTINGS,false);
                            if (recordAudioGranted != null && recordAudioGranted && modifyAudioGranted != null && modifyAudioGranted) {
                                Log.i(TAG, "Permission granted");
                            }
                        }
                );

        audioPermissionRequest.launch(new String[] {
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS
        });
    }
}
