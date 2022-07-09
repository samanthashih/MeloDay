package com.example.meloday20.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.meloday20.R;
import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.example.meloday20.utils.GetDetails;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private static final String TAG = PostAdapter.class.getSimpleName();
    private final Context context;
    private final List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_track, parent, false); // pass it a “blueprint” of the view (reference to XML layout file)
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        String trackCoverImageUrl;
        String profilePicUrl;
        TextView tvPostUsername;
        TextView tvPostDate;
        TextView tvPostTitle;
        TextView tvPostArtist;
        ImageView ivPostCoverImage;
        ImageView ivLike;
        TextView tvLikeNum;
        ImageView ivComment;
        ImageView ivPostProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViewsAndValues(itemView);
        }

        private void initViewsAndValues(@NonNull View itemView) {
            itemView.setOnClickListener(this);
            ivPostProfilePic = itemView.findViewById(R.id.ivPostProfilePic);
            tvPostUsername = itemView.findViewById(R.id.tvPostUsername);
            tvPostDate = itemView.findViewById(R.id.tvPostDate);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostArtist = itemView.findViewById(R.id.tvPostArtist);
            ivPostCoverImage = itemView.findViewById(R.id.ivPostCoverImage);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvLikeNum = itemView.findViewById(R.id.tvLikeNum);
            ivComment = itemView.findViewById(R.id.ivComment);
        }

        public void bind(Post post) throws ParseException {

            tvPostTitle.setText(post.getTrackName());
            tvPostArtist.setText(post.getTrackArtists());
            tvPostUsername.setText(post.getUsername());
            tvPostDate.setText(post.getCreatedAtDate());
            tvLikeNum.setText(String.valueOf(post.getNumLikes()));

            profilePicUrl = post.getUser().getString("profilePicUrl");
            if (profilePicUrl != null) {
                Glide.with(context)
                        .load(profilePicUrl)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .transform(new RoundedCorners(100))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivPostProfilePic);
            } else {
                Glide.with(context)
                        .load(R.drawable.ic_baseline_account_circle_24)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivPostProfilePic);
            }

            trackCoverImageUrl = post.getTrackImageUrl();
            if (trackCoverImageUrl != null) {
                Glide.with(context)
                        .load(trackCoverImageUrl)
                        .placeholder(R.drawable.default_playlist_cover)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(ivPostCoverImage);
            }

            if (post.isLikedByUser()) {
                ivLike.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                ivLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!post.isLikedByUser()) {
                            likePost();
                        } else {
                            unLikePost();
                        }
                        tvLikeNum.setText(String.valueOf(post.getNumLikes()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                private void unLikePost() throws ParseException {
                    Log.i(TAG, "Post liked before, now unlike");
                    post.deleteUserLikeOnPost();
                    ivLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }

                private void likePost() throws ParseException {
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
                    ivLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            });

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toComment = new Intent(context, CommentsActivity.class);
                    toComment.putExtra("post", Parcels.wrap(post));
                    context.startActivity(toComment);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Post post = posts.get(position);
//                Intent intent = new Intent(context, AddTrackActivity.class);
//                intent.putExtra("track", Parcels.wrap(track));
//                context.startActivity(intent);
                Log.i(TAG, "Clicked a post! " + post.getTrackName());
            }

        }
    }
}
