package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Room.Images;
import com.s13.codify.Room.ImagesRepo;
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
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImageClassifier extends Service {
    public static final int N_THREADS = 5;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        Context context = getApplicationContext();
        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Images> images = db.imagesDao().getImageListByImageStatusNotCheck(IMAGE_STATUS_NOT_CHECKED);
                for(Images image : images){
                    String imagePath = image.getImagePath();
                    System.out.println("Classifying image.....");
                    String label = classify(imagePath);
                    System.out.println("Classification complete !");
                    db.imagesDao().updateImageLabelByImagePath(imagePath,label);
                }
            }
        });
        return Service.START_NOT_STICKY;
    }

    public String classify(String filePath) {
        Bitmap bitmap = null;
        Module module = null;

        try {
            //Read the image as Bitmap
            bitmap = BitmapFactory.decodeFile(filePath);

            //reshape the image into 400*400
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

            //Loading the model file.
            module = Module.load(fetchModelFile(this, "model_traced.pt"));
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
