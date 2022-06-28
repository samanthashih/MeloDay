package com.example.meloday20;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        SpotifyService spotify = SpotifyServiceSingleton.getInstance();
        String trackTitle;
        List<ArtistSimple> trackArtists;
        Image trackCoverImage;

        TextView tvPostUsername;
        TextView tvPostTitle;
        TextView tvPostArtist;
        ImageView ivPostCoverImage;
        String artistsString;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvPostUsername = itemView.findViewById(R.id.tvPostUsername);
            tvPostTitle = itemView.findViewById(R.id.tvPostTitle);
            tvPostArtist = itemView.findViewById(R.id.tvPostArtist);
            ivPostCoverImage = itemView.findViewById(R.id.ivPostCoverImage);
            artistsString = "";

        }

        public void bind(Post post) {

            spotify.getTrack(post.getTrackId(), new Callback<Track>() {
                @Override
                public void success(Track track, Response response) {
                    trackTitle = track.name;
                    trackArtists = track.artists;
                    trackCoverImage = track.album.images.get(0);

                    tvPostTitle.setText(trackTitle);
                    artistsString = trackArtists.get(0).name;
                    if (trackArtists.size() > 1) {
                        for (int i = 1; i < trackArtists.size(); i++) {
                            artistsString = artistsString + ", " + trackArtists.get(i).name;
                        }
                    }
                    tvPostArtist.setText(artistsString);

                    if (trackCoverImage != null) {
                        Glide.with(context)
                                .load(trackCoverImage.url)
                                .into(ivPostCoverImage);
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Error getting track details.", error);
                }
            });

        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Clicked a post!");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Post post = posts.get(position);
//                Intent intent = new Intent(context, AddTrackActivity.class);
//                intent.putExtra("track", Parcels.wrap(track));
//                context.startActivity(intent);
            }
        }
    }
}
