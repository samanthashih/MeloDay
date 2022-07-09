package com.example.meloday20.home;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.meloday20.R;
import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private static final String TAG = CommentAdapter.class.getSimpleName();
    private final Context context;
    private final List<Comment> comments;

    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false); // pass it a “blueprint” of the view (reference to XML layout file)
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        try {
            holder.bind(comment);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivCommentProfile;
        TextView tvCommentUser;
        TextView tvCommentMessage;
        TextView tvCommentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViewsAndValues(itemView);
        }

        private void initViewsAndValues(@NonNull View itemView) {
            itemView.setOnClickListener(this);
            ivCommentProfile = itemView.findViewById(R.id.ivCommentProfile);
            tvCommentUser = itemView.findViewById(R.id.tvCommentUser);
            tvCommentMessage = itemView.findViewById(R.id.tvCommentMessage);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
        }

        public void bind(Comment comment) throws ParseException {
            String profilePicUrl = comment.getUser().getString("profilePicUrl");
            if (profilePicUrl != null && !profilePicUrl.equals("")) {
                Glide.with(context)
                        .load(profilePicUrl)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .transform(new RoundedCorners(100))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivCommentProfile);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_baseline_account_circle_24)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivCommentProfile);
            }

            tvCommentUser.setText(comment.getUser().getUsername());
            tvCommentMessage.setText(comment.getMessage());
            tvCommentDate.setText(comment.getCreatedAtDate());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Log.i(TAG, "Clicked a comment");
            }

        }
    }
}
