package com.example.meloday20.ui.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.service.SpotifyServiceSingleton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SpotifyLoginViewModel extends AndroidViewModel {
    private static final String TAG = SpotifyLoginViewModel.class.getSimpleName();
    private static SpotifyService spotify;
    private static String username;
    private MutableLiveData<Boolean> _loginUser = new MutableLiveData<>();
    LiveData<Boolean> loginUser = _loginUser;

    public SpotifyLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void loginUser(String accessToken) {
        spotify = SpotifyServiceSingleton.getInstance(accessToken);
        Log.i(TAG, accessToken);
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                username = userPrivate.id;
                loginUser(username, "password", accessToken);
                _loginUser.setValue(true);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    private void loginUser(String username, String password, String accessToken) {
        Log.i(TAG, "Login attempt for user: " + username);
        try {
            ParseUser.logIn(username, password);
        } catch (ParseException e) {
            signUpUser(username, password, accessToken);
            return;
        }
        setParseUserAccessToken(accessToken);
    }

    private void signUpUser(String username, String password, String accessToken) {
        Log.i(TAG, "Sign up attempt for user: " + username);
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate userPrivate, Response response) {
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        setParseUserAccessToken(accessToken);
                        if (userPrivate.images.size() > 0) {
                            setParseUserPfp(userPrivate.images.get(0).url);
                        }
                        Log.i(TAG, "signed up user: " + username);
                        return;
                    }
                });
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Could not get user info", error);
            }
        });
    }

    private void setParseUserAccessToken(String accessToken) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("accessToken", accessToken);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving accessToken", e);
                }
                Log.i(TAG, "Logged in: " + currentUser.getUsername());
            }
        });
    }

    private void setParseUserPfp(String pfpUrl) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("profilePicUrl", pfpUrl);
        currentUser.put("alarmTime", "11:00 PM");
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Parse Error while saving pfp", e);
                }
            }
        });
    }
}
