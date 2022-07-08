package com.example.meloday20.home;

import com.example.meloday20.utils.GetDetails;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String TAG = Post.class.getSimpleName();
    public static final String KEY_USER = "user";
    public static final String KEY_TRACK_ID = "trackId";
    public static final String KEY_TRACK_NAME = "trackName";
    public static final String KEY_TRACK_ARTISTS = "trackArtists";
    public static final String KEY_TRACK_IMAGE_URL = "trackImageUrl";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getTrackId() {
        return getString(KEY_TRACK_ID);
    }
    public void setTrackId(String trackId) {
        put(KEY_TRACK_ID, trackId);
    }

    public String getTrackName() {
        return getString(KEY_TRACK_NAME);
    }
    public void setTrackName(String trackName) {
        put(KEY_TRACK_NAME, trackName);
    }

    public String getTrackArtists() {
        return getString(KEY_TRACK_ARTISTS);
    }
    public void setTrackArtists(String trackArtists) {
        put(KEY_TRACK_ARTISTS, trackArtists);
    }

    public String getTrackImageUrl() {
        return getString(KEY_TRACK_IMAGE_URL);
    }
    public void setTrackImageUrl(String trackImageUrl) {
        put(KEY_TRACK_IMAGE_URL, trackImageUrl);
    }

    public String getCreatedAtDate() {
        return GetDetails.getDateString(this.getCreatedAt());
    }

    public String getUsername() {
        return getUser().getUsername();
    }

    public int getNumLikes() throws ParseException {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_POST);
        query.whereEqualTo(Like.KEY_POST, this);
        return query.find().size();
    }

    public boolean isLikedByUser() throws ParseException {
        List<Like> likes = getUserLikeOnPost();
        if (likes != null && likes.size() > 0) {
            return true;
        }
        return false;
    }

    private List<Like> getUserLikeOnPost() throws ParseException {
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.include(Like.KEY_USER);
        query.include(Like.KEY_POST);
        query.whereEqualTo(Like.KEY_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(Like.KEY_POST, this);
        query.setLimit(1);
        return query.find();
    }

    public void deleteUserLikeOnPost() throws ParseException {
        List<Like> likes = getUserLikeOnPost();
        if (likes != null && likes.size() > 0) {
            Like like = likes.get(0);
            like.delete();
            like.saveInBackground();
        }
    }

    public List<Comment> getPostComments() throws ParseException {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.include(Comment.KEY_POST);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.whereEqualTo(Like.KEY_POST, this);
        return query.find();
    }
}
