//package com.example.meloday20;
//
//
//import androidx.annotation.NonNull;
//import androidx.room.ColumnInfo;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//import com.example.meloday20.utils.GetDetails;
//
//import java.text.ParseException;
//
//import kaaes.spotify.webapi.android.models.PlaylistTrack;
//import kaaes.spotify.webapi.android.models.Track;
//
//public class MelodayPlaylistTrack {
//
//        @ColumnInfo
//        @PrimaryKey
//        @NonNull
//        public String trackId;
//
//        @ColumnInfo
//        public String name;
//
//        @ColumnInfo
//        public String artists;
//
//        @ColumnInfo
//        public String added_at;
//
//        @ColumnInfo
//        public String coverImageUrl;
//
//        public MelodayPlaylistTrack() {}
//
//        public MelodayPlaylistTrack fromTrack(PlaylistTrack playlistTrack) throws ParseException {
//            MelodayPlaylistTrack melodayTrack = new MelodayPlaylistTrack();
//            melodayTrack.trackId = playlistTrack.track.id;
//            melodayTrack.name = playlistTrack.track.name;
//            melodayTrack.artists = GetDetails.getArtistsString(playlistTrack.track.artists);
//            melodayTrack.added_at = GetDetails.getSpotifyDateString(playlistTrack.added_at);
//            melodayTrack.coverImageUrl = playlistTrack.track.album.images.get(0).url;
//            return melodayTrack;
//        }
//
//    }
//}
