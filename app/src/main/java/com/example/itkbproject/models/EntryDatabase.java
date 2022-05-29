package com.example.itkbproject.models;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.itkbproject.Entry;


@Database(entities= Entry.class, exportSchema = false, version = 1)
public abstract class EntryDatabase extends RoomDatabase {

    public static final String DB_NAME = "itkbDB";
    private static EntryDatabase instance;

    public static synchronized EntryDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),EntryDatabase.class,DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract EntryDao entryDao();

}