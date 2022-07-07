package com.example.meloday20;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities={MeloDayPlaylistTrack.class}, version=1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MeloDayPlaylistTrackDao meloDayPlaylistTrackDao();

    public static final String NAME = "MyDataBase";
}
