package com.example.meloday20.ui.home.comment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.meloday20.R;
import com.example.meloday20.models.Comment;
import com.example.meloday20.models.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;

import org.parceler.Parcels;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
    private CommentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        init();
        try {
            displayComments();
            Log.i(TAG, comments.get(0).getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        typedComment();
    }
    private void init() {
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
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

    private void typedComment() {
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
                            viewModel.submitComment(s, post);
                            Observer<Boolean> submitCommentObserver = new Observer<Boolean>() {
                                @Override
                                public void onChanged(Boolean submitComment) {
                                    try {
                                        displayComments();
                                    } catch (ParseException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            };
                            viewModel.submitComment.observe(CommentsActivity.this,submitCommentObserver);
                            etMessageLayout.getEditText().setText(null);
                        }
                    });
                }
            }
        });
    }

    private void displayComments() throws ParseException {
        comments.clear();
        comments.addAll(post.getPostComments());
        adapter.notifyDataSetChanged();
    }
}