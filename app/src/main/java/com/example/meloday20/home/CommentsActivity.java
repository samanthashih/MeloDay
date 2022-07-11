package com.example.meloday20.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.meloday20.R;
import com.example.meloday20.search.SearchAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class CommentsActivity extends AppCompatActivity {
    private static String TAG = CommentsActivity.class.getSimpleName();
    private List<Comment> comments;
    private CommentAdapter adapter;
    private Post post;
    private RecyclerView rvComments;
    private TextInputLayout etMessageLayout;
    private TextInputEditText etMessage;
    private ImageView ivCommentSubmit;
    private LinearLayoutManager linearLayoutManager;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        initViews();

        try {
            getPostComments();
            Log.i(TAG, comments.get(0).getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > etMessageLayout.getCounterMaxLength()) {
                    etMessageLayout.setError("Max character length is " + etMessageLayout.getCounterMaxLength());
                } else {
                    ivCommentSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Comment comment = new Comment();
                            comment.setUser(currentUser);
                            comment.setPost(post);
                            comment.setMessage(s.toString());

                            comment.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "Issue saving comment" , e);
                                        return;
                                    }
                                    try {
                                        getPostComments();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                    Log.i(TAG, "Posted comment");                                }
                            });
                            etMessageLayout.getEditText().setText(null);
                        }
                    });
                }
            }
        });
    }

    private void initViews() {
        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        Log.i(TAG, post.getTrackName());
        rvComments = findViewById(R.id.rvComments);
        etMessageLayout = findViewById(R.id.etMessageLayout);
        etMessage = findViewById(R.id.etMessage);
        ivCommentSubmit = findViewById(R.id.ivCommentSubmit);

        comments = new ArrayList<>();
        adapter = new CommentAdapter(this, comments);
        linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(linearLayoutManager);
    }

    private void getPostComments() throws ParseException {
        comments.clear();
        comments.addAll(post.getPostComments());
        adapter.notifyDataSetChanged();
    }
}