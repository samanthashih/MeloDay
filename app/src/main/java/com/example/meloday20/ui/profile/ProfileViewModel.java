package com.example.meloday20.ui.profile;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.R;
import com.example.meloday20.models.AlarmTime;
import com.example.meloday20.models.ParsePlaylist;
import com.example.meloday20.service.AlarmBroadcastReceiver;
import com.example.meloday20.service.SpotifyServiceSingleton;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Artists;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = ProfileViewModel.class.getSimpleName();
    private static String accessToken = ParseUser.getCurrentUser().getString("accessToken");
    public static SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
    private final ParseUser currentUser = ParseUser.getCurrentUser();
    private Context context;
    private HashMap<String, Integer> genreCountMap = new HashMap<>();

    private List<String> listLegendLabels = new ArrayList<>();
    private MutableLiveData<List<String>> _legendLabels = new MutableLiveData<>();
    public LiveData<List<String>> legendLabels = _legendLabels;

    private List<PieModel> listPieModels = new ArrayList<>();
    private MutableLiveData<List<PieModel>> _pieModels = new MutableLiveData<>();
    public LiveData<List<PieModel>> pieModels = _pieModels;



    public ProfileViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void getCurrUserPlaylistGenres(String[] colors) {
        ParseQuery<ParsePlaylist> query = ParseQuery.getQuery(ParsePlaylist.class); // specify what type of data we want to query - ParsePlaylist.class
        query.whereEqualTo(ParsePlaylist.KEY_USER, currentUser);
        query.include(ParsePlaylist.KEY_PLAYLIST_ID); // include data referred by current user
        try {
            String currentUsersPlaylistId = query.find().get(0).getPlaylistId();
            getPlaylistGenreCount(currentUser.getUsername(), currentUsersPlaylistId, colors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPlaylistGenreCount(String userId, String playlistId, String[] colors) {
        spotify.getPlaylistTracks(userId, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrackPager, Response response) {
                String artists = "";
                for (PlaylistTrack playlistTrack: playlistTrackPager.items) {
                    artists += playlistTrack.track.artists.get(0).id + ",";
                }
                spotify.getArtists(artists.substring(0,artists.length()-1), new Callback<Artists>() {
                    @Override
                    public void success(Artists artists, Response response) {
                        for (Artist artist: artists.artists) {
                            List<String> genres = artist.genres;
                            if (genres.size() > 0) {
                                for (String genre : genres) {
                                    genreCountMap.merge(genre, 1, Integer::sum);
                                }
                            }
                        }

                        // Create a list from elements of HashMap
                        List<Map.Entry<String, Integer> > list
                                = new LinkedList<Map.Entry<String, Integer> >(
                                genreCountMap.entrySet());

                        // Sort the list using lambda expression
                        list.sort(Comparator.comparing(Map.Entry::getValue));
                        Collections.reverse(list);

                        for (Map.Entry entry : list) {
                            listLegendLabels.add(entry.getKey().toString());
                        }
                        _legendLabels.setValue(listLegendLabels);

                        int colorIndex = 0;
                        for (int i = 0; i < 10; i++) {
                            Map.Entry<String, Integer> entry = list.get(i);
                            listPieModels.add(
                                    new PieModel(
                                        entry.getKey(),
                                        entry.getValue(),
                                        Color.parseColor(colors[colorIndex]))
                            );
                            colorIndex++;
                        }
                        Log.i(TAG, "length of list pie models:" + listPieModels.size());
                        _pieModels.setValue(listPieModels);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i(TAG, "Error getting artist", error);
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Error getting track details.", error);
            }
        });
    }

    public void createAndSaveNotification(String inputTime) {
        AlarmTime alarmTime = new AlarmTime(inputTime);
        saveAlarmTimeInParse(inputTime);
        createAlarmNotification(alarmTime.getHour(), alarmTime.getMinute());
    }

    private void saveAlarmTimeInParse(String inputTime) {
        currentUser.put(context.getString(R.string.keyAlarmTime), inputTime);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue saving alarm time" , e);
                    return;
                }
                Log.i(TAG, "Alarm time was saved");
            }
        });
    }

    private void createAlarmNotification(int hour, int minute) {
        Log.i(TAG, "Create alarm at: " + hour + ":" + minute);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,  PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void createNotificationChannel() {
        String channelId = context.getString(R.string.channel_id);
        CharSequence channelName = context.getString(R.string.channel_name);
        String channelDescription = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void logoutUser() {
        ParseUser.logOutInBackground();
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put(context.getString(R.string.keyAccessToken), "");
        try {
            currentUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not delete accessToken");
        }
    }
}
