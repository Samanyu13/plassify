package com.s13.codify.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//import com.example.memedetection.Repository.ImageRepo;

@Database(entities = {Images.class}, version = 1, exportSchema=true)
public abstract class ImagesRoomDatabase extends RoomDatabase {

    private static volatile ImagesRoomDatabase INSTANCE;

    public static ImagesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ImagesRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImagesRoomDatabase.class, "imagesDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ImagesDao imagesDao();

}