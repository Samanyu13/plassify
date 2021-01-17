package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.s13.codify.Room.Images;
import com.s13.codify.Room.ImagesRoomDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImageFinder extends Service {

    public static final int N_THREADS = 5;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("SCHEDULER SAYS HELLO SOMA !");
                int column_index_data;
                String absolutePathOfImage;

                Context context = getApplicationContext();
                ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);

                Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {MediaStore.MediaColumns.DATA};

                String orderBy = MediaStore.Video.Media.DATE_TAKEN;
                Cursor cursor = context.getContentResolver().query(uri, projection, null,
                        null, orderBy+" DESC");
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

                while (cursor.moveToNext()) {
                    absolutePathOfImage = cursor.getString(column_index_data);
                    Images image = new Images(absolutePathOfImage,IMAGE_STATUS_NOT_CHECKED);
                    db.imagesDao().insert(image);
                }

            }
        });

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
