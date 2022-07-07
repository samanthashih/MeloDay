package com.example.meloday20;

import androidx.room.Room;

import com.example.meloday20.utils.ParseApplication;

public class MyDatabaseApplication extends ParseApplication {
    MyDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).fallbackToDestructiveMigration().build();
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}
