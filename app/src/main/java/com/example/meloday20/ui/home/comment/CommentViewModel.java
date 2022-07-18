package com.example.meloday20.ui.home.comment;

import android.app.Application;
import android.text.Editable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.meloday20.models.Comment;
import com.example.meloday20.models.Post;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentViewModel extends AndroidViewModel {
    private static final String TAG = CommentViewModel.class.getSimpleName();
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private MutableLiveData<Boolean> _submitComment = new MutableLiveData<>();
    LiveData<Boolean> submitComment = _submitComment;

    public CommentViewModel(@NonNull Application application) {
        super(application);
    }

    public void submitComment(Editable s, Post post) {
        Comment comment = new Comment();
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.setMessage(s.toString());
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue saving comment", e);
                    return;
                }
                _submitComment.setValue(true);
//                try {
//                    getPostComments();
//                } catch (ParseException ex) {
//                    ex.printStackTrace();
//                }
//                Log.i(TAG, "Posted comment");                                }
//        });
            }
        });
    }
}
