package com.example.meloday20.home.post;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.meloday20.home.HomeFragment;
import com.example.meloday20.utils.PreviewPlayer;
import com.example.meloday20.R;
import com.example.meloday20.audioWaveAnimation.RendererFactory;
import com.example.meloday20.audioWaveAnimation.WaveFormView;
import com.example.meloday20.home.comment.CommentsActivity;
import com.example.meloday20.utils.CommonActions;
import com.example.meloday20.utils.OnDoubleTapListener;
import com.example.meloday20.utils.PreviewPlayerSingleton;
import com.example.meloday20.utils.SpotifyServiceSingleton;
import com.example.meloday20.utils.VisualizerSingleton;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
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
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Visualizer.OnDataCaptureListener {
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
        ImageView ivPostPlayArrow;
        CardView card_view;
        private String accessToken = ParseUser.getCurrentUser().getString("accessToken");
        public SpotifyService spotify = SpotifyServiceSingleton.getInstance(accessToken);
        public PreviewPlayer previewPlayer = PreviewPlayerSingleton.getInstance();
        public Visualizer visualizer;

        private static final int CAPTURE_SIZE = 256;
        private int audioSession;
        private WaveFormView waveformView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViewsAndValues(itemView) ;
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
            ivPostPlayArrow = itemView.findViewById(R.id.ivPostPlayArrow);
            card_view = itemView.findViewById(R.id.card_view);
            waveformView = itemView.findViewById(R.id.waveform_view);
            RendererFactory rendererFactory = new RendererFactory();
            waveformView.setRenderer(rendererFactory.createSimpleWaveformRenderer(Color.WHITE, Color.TRANSPARENT));
        }

        public void bind(Post post) throws ParseException {
            tvPostTitle.setText(post.getTrackName());
            tvPostArtist.setText(post.getTrackArtists());
            tvPostUsername.setText(post.getUsername());
            tvPostDate.setText(post.getCreatedAtDate());
            tvLikeNum.setText(String.valueOf(post.getNumLikes()));
            waveformView.setVisibility(View.GONE);

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
                    likeAction(post);
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

            card_view.setOnTouchListener(new OnDoubleTapListener(context) {
                @Override
                public void onDoubleTap(MotionEvent e) {
                    Log.i(TAG, "double tap post");
                    likeAction(post);
                }
            });

            ivPostCoverImage.setOnClickListener(new View.OnClickListener() {
                // get track preview url
                @Override
                public void onClick(View v) {
                    ivPostPlayArrow.setVisibility(View.GONE);
                    spotify.getTrack(post.getTrackId(), new Callback<Track>() {
                        @Override
                        public void success(Track track, Response response) {
                            String previewUrl = track.preview_url;
                            if (previewPlayer.isPlaying()){
                                if (previewPlayer.getCurrentTrack().equals(previewUrl)) {
                                    stopPlayingCurrTrack();
                                    return;
                                }
                                stopPlayingCurrTrack();
                            }
                            startPlayingPreview(previewUrl);
                            audioSession = previewPlayer.getAudioSessionId();
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
                                startVisualizer();
                            } else {
                                CommonActions.requestPermissions(new HomeFragment());
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, "Error with getting preview url", error);
                        }
                    });
                }
            });
        }

        private void startPlayingPreview(String previewUrl) {
            previewPlayer.play(previewUrl);
            ivPostPlayArrow.setVisibility(View.GONE);
            waveformView.setVisibility(View.VISIBLE);
        }

        private void stopPlayingCurrTrack() {
            previewPlayer.release();
            ivPostPlayArrow.setVisibility(View.VISIBLE);
            if (visualizer != null) visualizer.release();
            waveformView.setVisibility(View.GONE);
        }

        private void startVisualizer() {
            Log.i(TAG, "Audio session: " + audioSession);
            visualizer = VisualizerSingleton.getInstance(audioSession);
            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
            visualizer.setCaptureSize(CAPTURE_SIZE);
            visualizer.setEnabled(true);
        }

        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            if (waveformView != null) {
                waveformView.setWaveform(waveform);
            }
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            // Fast Fournier Transform data - don't need for this
        }

        private void likeAction(Post post) {
            try {
                if (!post.isLikedByUser()) {
                    CommonActions.likePost(post);
                    ivLike.setImageResource(R.drawable.ic_baseline_favorite_24);
                } else {
                    CommonActions.unLikePost(post);
                    ivLike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                tvLikeNum.setText(String.valueOf(post.getNumLikes()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
                Post post = posts.get(position);
                Log.i(TAG, "Clicked a post! " + post.getTrackName());
            }

        }
    }
}
