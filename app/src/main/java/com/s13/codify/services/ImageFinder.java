package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.s13.codify.Room.Images.Images;
import com.s13.codify.Room.ImagesRoomDatabase;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImageFinder extends Service {

    public static final int N_THREADS = 5;
    private static boolean isRunning;

    @Override
    public void onCreate(){
        super.onCreate();
        isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    isRunning = true;
//                    Cursor cursor = null;
                    try {

                        int column_index_data;
                        String absolutePathOfImage;

                        Context context = getApplicationContext();
                        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);

                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        Date lastClassifiedTime = db.classificationDao().getLastClassifiedTimestamp();
                        String[] projection = {
                                MediaStore.MediaColumns.DATA,
                                MediaStore.Images.Media.DISPLAY_NAME,
                                MediaStore.Images.Media.DATE_MODIFIED
                        };
                        String selection = new String();
                        String[] selectionArgs = new String[1];
                        if (lastClassifiedTime == null) {
                            selectionArgs = null;
                            selection = null;
                        } else {
                            selection = "date_modified >= ?";
                            selectionArgs[0] = String.valueOf(dateToTimestamp(lastClassifiedTime));
                        }
                        String orderBy = "date_modified DESC";
                        try(Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy)) {
                            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                            String[] count = cursor.getColumnNames();
                            while (cursor.moveToNext()) {
                                absolutePathOfImage = cursor.getString(column_index_data);
                                Images image = new Images(absolutePathOfImage);
                                System.out.println("Inserting image");
                                db.imagesDao().insert(image);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Exception: --> " + e);
                    }
//                    } finally {
//                        cursor.close();
//                    }
                    isRunning = false;
                }
            });
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public long dateToTimestamp(Date date){
        TimeUnit t = TimeUnit.MILLISECONDS;
        return t.toSeconds(date == null ? 0L: date.getTime());
    }

}
