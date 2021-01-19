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
                    String label = theClassifier(absolutePathOfImage);
                    Images image = new Images(absolutePathOfImage,label);
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

    public String theClassifier(String filePath) {
        Bitmap bitmap = null;
        Module module = null;

        try {
            //Read the image as Bitmap
            bitmap = BitmapFactory.decodeFile(filePath);

            //reshape the image into 400*400
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

            //Loading the model file.
            module = Module.load(fetchModelFile(ImageFinder.this, "model_traced.pt"));
        } catch (IOException e) {
            System.out.println("APP SAYS PODA SOMA !");
        }

        //Input Tensor
        final Tensor input = TensorImageUtils.bitmapToFloat32Tensor(
                bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
                TensorImageUtils.TORCHVISION_NORM_STD_RGB
        );

        //Calling the forward of the model to run our input
        final Tensor output = module.forward(IValue.from(input)).toTensor();


        final float[] score_arr = ((Tensor) output).getDataAsFloatArray();

        // Fetch the index of the value with maximum score
        float max_score = -Float.MAX_VALUE;
        int ms_ix = -1;
        for (int i = 0; i < score_arr.length; i++) {
            if (score_arr[i] > max_score) {
                max_score = score_arr[i];
                ms_ix = i;
            }
        }

        //Fetching the name from the list based on the index
        String detectedClass = ModelClasses.MODEL_CLASSES[ms_ix];
        return detectedClass;


    }

    public static String fetchModelFile(Context context, String modelName) throws IOException {
        File file = new File(context.getFilesDir(), modelName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(modelName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
}
