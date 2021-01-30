package com.s13.codify.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.s13.codify.Room.Classifications.Classification;
import com.s13.codify.Room.Classifications.ClassificationDao;
import com.s13.codify.Room.Images.Images;
import com.s13.codify.Room.Images.ImagesDao;
import com.s13.codify.Room.ModelClasses.ModelClass;
import com.s13.codify.Room.ModelClasses.ModelClassesDao;

//import com.example.memedetection.Repository.ImageRepo;

@Database(entities = {Images.class, ModelClass.class, Classification.class}, version = 1, exportSchema=true)
@TypeConverters({Converters.class})
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

    public abstract ModelClassesDao modelClassesDao();

    public abstract ClassificationDao classificationDao();

}