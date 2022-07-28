package com.example.meloday20.ui.addTrack;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.models.Post;
import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.utils.GetDetails;
import com.example.meloday20.service.SpotifyServiceSingleton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.SnapshotId;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackToRemove;
import kaaes.spotify.webapi.android.models.TracksToRemove;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddTrackViewModel extends AndroidViewModel {
    private static final String TAG = AddTrackViewModel.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    private static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private static ParseUser currentUser = ParseUser.getCurrentUser();
    private static String userId = currentUser.getUsername();
    private static Track currTrack;
    private static String playlistId;
    private static String lastPostedDate;
    private static String today;
    private MutableLiveData<Boolean> _postedToday = new MutableLiveData<>();
    LiveData<Boolean> postedToday = _postedToday;

    public AddTrackViewModel(@NonNull Application application) {
        super(application);
    }

    public void addTrackActions(Track track) {
        currTrack = track;
        createTrackPost();
        addTrackToPlaylist();
    }

    private void addTrackToPlaylist() {
        // Specify what type of data we want to query - ParsePlaylist.class
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class);
        query.whereEqualTo(ParsePlaylist.KEY_USER, ParseUser.getCurrentUser());
        query.include(ParsePlaylist.KEY_PLAYLIST_ID);
        // Find current user's playlist ID
        query.findInBackground(new FindCallback<ParsePlaylist>() {
            @Override
            public void done(List<ParsePlaylist> queryPlaylists, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting playlist ID", e);
                    return;
                }
                playlistId = queryPlaylists.get(0).getPlaylistId();

                Map<String, Object> addTrackQueryMap = new HashMap<>();
                Map<String, Object> addTrackBody = new HashMap<>();
                addTrackBody.put("uris", new String[]{"spotify:track:" + currTrack.id});
                spotify.addTracksToPlaylist(userId, playlistId, addTrackQueryMap, addTrackBody, new Callback<Pager<PlaylistTrack>>() {
                    @Override
                    public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                        Log.i(TAG, "Added track to playlist");
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.d(TAG, error.toString());
                    }
                });
            }
        });
    }

    private void createTrackPost() {
        Post post = new Post();
        post.setUser(currentUser);
        post.setTrackId(currTrack.id);
        post.setTrackName(currTrack.name);
        post.setTrackArtists(GetDetails.getArtistsString(currTrack.artists));
        post.setTrackImageUrl(currTrack.album.images.get(0).url);

        // Save post in Parse DB
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post", e);
                }
                Log.i(TAG, "Post save success");
            }
        });
    }

    public void checkIfPostedToday() {
        Post lastPost = getUserLastPost();
        if (lastPost != null) {
            lastPostedDate = GetDetails.getDateString(lastPost.getCreatedAt());
            today = GetDetails.getDateString(new Date());
            Log.i(TAG, lastPostedDate + "vs. today: "+ today);
            if (lastPostedDate.equals(today)) {
                Log.i(TAG, "Already posted today");
                _postedToday.setValue(true);
            } else {
                Log.i(TAG, "Did not post today");
                _postedToday.setValue(false);
            }
        } else {
            Log.e(TAG, "Error getting user's last post");
            _postedToday.setValue(false);
        }
    }

    public Post getUserLastPost() {
        // Specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, currentUser);
        // Get the newer posts first
        query.addDescendingOrder("createdAt");
        // Get only the latest post
        query.setLimit(1);
        query.include(Post.KEY_TRACK_ID);
        try {
            return query.find().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteTodayPost() throws ParseException {
        String trackIdToDelete = deletePostFromParse();
        deleteTrackFromPlaylist(trackIdToDelete);
    }

    private void deleteTrackFromPlaylist(String trackIdToDelete) {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        parcel.writeValue(trackIdToDelete);
        TrackToRemove t = new TrackToRemove();
        t.CREATOR.createFromParcel(parcel);

        List<TrackToRemove> removeTracks = new ArrayList<>();
        removeTracks.add(t);
        android.os.Parcel parcel2 = android.os.Parcel.obtain();
        parcel2.writeValue(removeTracks);
        TracksToRemove tracksToRemove = new TracksToRemove();
        tracksToRemove.CREATOR.createFromParcel(parcel2);
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
                spotify.removeTracksFromPlaylist(userId, playlistId, tracksToRemove, new Callback<SnapshotId>() {
                    @Override
                    public void success(SnapshotId snapshotId, Response response) {
                        Log.i(TAG, "Removed today's track from playlist");
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, "Could not remove song from playlist", error);
                    }
                });
            }
        });
    }

    private String deletePostFromParse() throws ParseException {
        Post lastPost = getUserLastPost();
        lastPost.delete();
        lastPost.saveInBackground();
        String trackIdToDelete = lastPost.getTrackId();
        return trackIdToDelete;
    }
}
