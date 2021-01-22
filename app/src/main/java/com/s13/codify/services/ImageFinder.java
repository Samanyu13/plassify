package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Room.Images;
import com.s13.codify.Room.ImagesRoomDatabase;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
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
                    Cursor cursor = null;
                    try {

                        int column_index_data;
                        String absolutePathOfImage;

                        Context context = getApplicationContext();
                        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);

                        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                        Date lastClassifiedTime = db.imagesDao().getLastClassifiedTimestamp();
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
                        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);
                        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                        String[] count = cursor.getColumnNames();
                        while (cursor.moveToNext()) {
                            absolutePathOfImage = cursor.getString(column_index_data);
                            Images image = new Images(absolutePathOfImage, IMAGE_STATUS_NOT_CHECKED);
                            System.out.println("Inserting image");
                            db.imagesDao().insert(image);
                        }
                    } catch (Exception e) {

                    } finally {
                        cursor.close();
                    }
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
