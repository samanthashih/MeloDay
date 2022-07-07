package com.example.meloday20;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.meloday20.utils.GetDetails;

import java.text.ParseException;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

@Entity
public class MeloDayPlaylistTrack {

    @ColumnInfo
    @PrimaryKey
    @NonNull
    public String trackId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String artists;

    @ColumnInfo
    public String added_at;

    @ColumnInfo
    public String coverImageUrl;

    public MeloDayPlaylistTrack() {}

    public MeloDayPlaylistTrack fromTrack(PlaylistTrack playlistTrack) throws ParseException {
        MeloDayPlaylistTrack meloDayTrack = new MeloDayPlaylistTrack();
        meloDayTrack.trackId = playlistTrack.track.id;
        meloDayTrack.name = playlistTrack.track.name;
        meloDayTrack.artists = GetDetails.getArtistsString(playlistTrack.track.artists);
        meloDayTrack.added_at = GetDetails.getSpotifyDateString(playlistTrack.added_at);
        meloDayTrack.coverImageUrl = playlistTrack.track.album.images.get(0).url;
        return meloDayTrack;
    }

}
