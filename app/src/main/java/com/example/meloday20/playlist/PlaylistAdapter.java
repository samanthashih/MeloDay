package com.example.meloday20.playlist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.meloday20.MeloDayPlaylistTrack;
import com.example.meloday20.R;
import com.example.meloday20.utils.GetDetails;

import java.text.ParseException;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class PlaylistAdapter  extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{
    private static final String TAG = PlaylistAdapter.class.getSimpleName();
    private final Context context;
    private final List<MeloDayPlaylistTrack> playlistTracks;

    public PlaylistAdapter(Context context, List<MeloDayPlaylistTrack> playlistTracks) {
        this.context = context;
        this.playlistTracks = playlistTracks;
    }

    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist_track, parent, false); // pass it a “blueprint” of the view (reference to XML layout file)
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        MeloDayPlaylistTrack playlistTrack = playlistTracks.get(position);
        try {
            holder.bind(playlistTrack);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return playlistTracks.size();
    }

    // viewholder class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvPlaylistTrackDate;
        TextView tvPlaylistTitle;
        TextView tvPlaylistArtist;
        ImageView ivPlaylistCoverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViewsAndValues(itemView);
        }

        private void initViewsAndValues(@NonNull View itemView) {
            itemView.setOnClickListener(this);
            tvPlaylistTrackDate = itemView.findViewById(R.id.tvPlaylistTrackDate);
            tvPlaylistTitle = itemView.findViewById(R.id.tvPlaylistTitle);
            tvPlaylistArtist = itemView.findViewById(R.id.tvPlaylistArtist);
            ivPlaylistCoverImage = itemView.findViewById(R.id.ivPlaylistCoverImage);
        }

        public void bind(MeloDayPlaylistTrack playlistTrack) throws ParseException {
//            Track track = playlistTrack.track;
            Log.i(TAG, playlistTrack.name);
            tvPlaylistTrackDate.setText(playlistTrack.added_at);
            tvPlaylistTitle.setText(playlistTrack.name);
            tvPlaylistArtist.setText(playlistTrack.artists);
            String coverImageUrl = playlistTrack.coverImageUrl;
            if (coverImageUrl != null) {
                Glide.with(context)
                        .load(coverImageUrl)
                        .into(ivPlaylistCoverImage);
            }
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Clicked a playlist track!");
            int position = getAdapterPosition();
//            if (position != RecyclerView.NO_POSITION) { //if position valid, get that post
//                PlaylistTrack playlistTrack = playlistTracks.get(position);
//            }
        }
    }
}

