package com.example.meloday20.utils;

import android.util.Log;

import com.example.meloday20.home.CommentsActivity;
import com.example.meloday20.home.Like;
import com.example.meloday20.home.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommonActions {
    private static String TAG = CommonActions.class.getSimpleName();

    public static void likePost(Post post) throws ParseException {
        Log.i(TAG, "Post not liked before, now like");
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
                Log.i(TAG, "Like was saved!!");
            }
        });
    }

    public static void unLikePost(Post post) throws ParseException {
        Log.i(TAG, "Post liked before, now unlike");
        post.deleteUserLikeOnPost();
    }
}
