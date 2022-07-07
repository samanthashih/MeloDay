package com.example.meloday20;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeloDayPlaylistTrackDao {
    @Query("SELECT * FROM MeloDayPlaylistTrack LIMIT 365")
    public List<MeloDayPlaylistTrack> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeloDayPlaylistTrack(MeloDayPlaylistTrack... meloDayPlaylistTracks);
}
