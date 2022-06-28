package com.example.meloday20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddTrackActivity extends AppCompatActivity {
    private static final String TAG = AddTrackActivity.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private static ParseUser currentUser;
    private static String userId;
    private static String playlistId;
    private static Track track;
    private static String artistsString;
    TextView tvAddTrackTitle;
    TextView tvAddTrackArtist;
    ImageView ivAddTrackCover;
    Button btnAddTrackToPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);
        track = Parcels.unwrap(getIntent().getParcelableExtra("track"));
        initViews();
        setViewValues();
    }

    private void createTrackPost() {
        Post post = new Post();
        post.setUser(currentUser);
        post.setTrackId(track.id);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error while saving post", e);
                }
                Log.i(TAG, "post save success");
            }
        });
    }

    private void setViewValues() {
        tvAddTrackTitle.setText(track.name);
        artistsString = track.artists.get(0).name;
        if (track.artists.size() > 1) {
            for (int i = 1; i < track.artists.size(); i++) {
                artistsString = artistsString + ", " + track.artists.get(i).name;
            }
        }
        tvAddTrackArtist.setText(artistsString);
        Image coverImage = track.album.images.get(0);
        if (coverImage != null) {
            Glide.with(this)
                    .load(coverImage.url)
                    .into(ivAddTrackCover);
        }
    }

    private void initViews() {
        currentUser = ParseUser.getCurrentUser();
        tvAddTrackTitle = findViewById(R.id.tvAddTrackTitle);
        tvAddTrackArtist = findViewById(R.id.tvAddTrackArtist);
        ivAddTrackCover = findViewById(R.id.ivAddTrackCover);
        btnAddTrackToPlaylist = findViewById(R.id.btnAddTrackToPlaylist);
        btnAddTrackToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTrackToPlaylist();
                createTrackPost();
            }
        });
    }

    private void addTrackToPlaylist() {
        userId = currentUser.getUsername();
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class); // specify what type of data we want to query - ParsePlaylist.class
        query.whereEqualTo(ParsePlaylist.KEY_USER, ParseUser.getCurrentUser());
        query.include(ParsePlaylist.KEY_PLAYLIST_ID); // include data referred by current user
        query.findInBackground(new FindCallback<ParsePlaylist>() {
            @Override
            public void done(List<ParsePlaylist> queryPlaylists, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting playlist id", e);
                    return;
                }
                playlistId = queryPlaylists.get(0).getPlaylistId();

                Map<String, Object> addTrackQueryMap = new HashMap<>();
                Map<String, Object> addTrackBody = new HashMap<>();
                addTrackBody.put("uris", new String[]{"spotify:track:" + track.id});
                spotify.addTracksToPlaylist(userId, playlistId, addTrackQueryMap, addTrackBody, new Callback<Pager<PlaylistTrack>> () {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        Log.i(TAG, "Added song to playlist");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, error.toString());
                    }
                });
            }
        });


    }
}