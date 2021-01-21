package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Room.Images;
import com.s13.codify.Room.ImagesRepo;
import com.s13.codify.Room.ImagesRoomDatabase;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;
import static com.s13.codify.Utils.Constants.MAX_RESULTS;
import static java.lang.Integer.min;
import static org.checkerframework.checker.units.UnitsTools.min;

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

                FirebaseCustomRemoteModel remoteModel = new FirebaseCustomRemoteModel.Builder("Image-classifier").build();
                FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
                        .addOnCompleteListener(new OnCompleteListener<File>() {
                            @Override
                            public void onComplete(@NonNull Task<File> task) {
                                File modelFile = task.getResult();
                                if (modelFile != null) {
                                    Interpreter interpreter = new Interpreter(modelFile);

                                    List<Images> images = db.imagesDao().getImageListByImageStatusNotCheck(IMAGE_STATUS_NOT_CHECKED);
                                    for (Images image : images) {
                                        String imagePath = image.getImagePath();
                                        System.out.println("Classifying image.....");
                                        String label = classify(imagePath, interpreter);
                                        System.out.println("Classification complete !");
                                        db.imagesDao().updateImageLabelByImagePath(imagePath,label);
                                    }
                                }
                            }
                        });
            }
        });
        return Service.START_NOT_STICKY;
    }

    /** An immutable result returned by a Classifier describing what was recognized. */
    public static class Recognition {
        /**
         * A unique identifier for what has been recognized. Specific to the class, not the instance of
         * the object.
         */
        private final String id;

        /** Display name for the recognition. */
        private final String title;

        /**
         * A sortable score for how good the recognition is relative to others. Higher should be better.
         */
        private final Float confidence;

        /** Optional location within the source image for the location of the recognized object. */
        private RectF location;

        public Recognition(
                final String id, final String title, final Float confidence, final RectF location) {
            this.id = id;
            this.title = title;
            this.confidence = confidence;
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public Float getConfidence() {
            return confidence;
        }

        public RectF getLocation() {
            return new RectF(location);
        }

        public void setLocation(RectF location) {
            this.location = location;
        }

        @Override
        public String toString() {
            String resultString = "";
            if (id != null) {
                resultString += "[" + id + "] ";
            }

            if (title != null) {
                resultString += title + " ";
            }

            if (confidence != null) {
                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
            }

            if (location != null) {
                resultString += location + " ";
            }

            return resultString.trim();
        }
    }


    public String classify(String filePath, Interpreter interpreter) {
                                //Read the image as Bitmap
                                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                                //reshape the image into 400*400
                                bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

                                // IP Image Buffer
                                DataType imageDataType = interpreter.getInputTensor(0).dataType();
                                TensorImage inputImageBuffer = new TensorImage(imageDataType);
                                inputImageBuffer.load(bitmap);

                                //OP Image Buffer
                                int[] probabilityShape = interpreter.getOutputTensor(0).shape();
                                DataType probabilityDataType = interpreter.getOutputTensor(0).dataType();
                                TensorBuffer outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);

                                interpreter.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());


                                // Creates the post processor for the output probability.
                                ;
                                TensorProcessor probabilityProcessor;
                                probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                                List<String> labels = new ArrayList<>(Arrays.asList(ModelClasses.MODEL_CLASSES));

                                Map<String, Float> labeledProbability = new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                                                .getMapWithFloatValue();

//                                for(Map.Entry<String, Float> xxx : labeledProbability.entrySet()) {
//                                    System.out.println(xxx.getKey() + ": KEY, VAL : " + xxx.getValue().toString());
//                                }

                                Recognition kuttaappi = getTopKProbability(labeledProbability);

                                return kuttaappi.getTitle();
//                                System.out.println("SIVANE ITH ETH JILLA");
                            }


    /** Gets the top-k results. */
    private static Recognition getTopKProbability(Map<String, Float> labelProb) {
        // Find the best classifications.
        PriorityQueue<Recognition> pq =
                new PriorityQueue<>(
                        MAX_RESULTS,
                        new Comparator<Recognition>() {
                            @Override
                            public int compare(Recognition lhs, Recognition rhs) {
                                // Intentionally reversed to put high confidence at the head of the queue.
                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                            }
                        });

        for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
            pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
        }

        Recognition recognition = pq.poll();
        return recognition;
    }

    protected TensorOperator getPostprocessNormalizeOp() {
        return new NormalizeOp(0.0f, 1.0f);
    }

//    public static String fetchModelFile(Context context, String modelName) throws IOException {
//        File file = new File(context.getFilesDir(), modelName);
//        if (file.exists() && file.length() > 0) {
//            return file.getAbsolutePath();
//        }
//
//        try (InputStream is = context.getAssets().open(modelName)) {
//            try (OutputStream os = new FileOutputStream(file)) {
//                byte[] buffer = new byte[4 * 1024];
//                int read;
//                while ((read = is.read(buffer)) != -1) {
//                    os.write(buffer, 0, read);
//                }
//                os.flush();
//            }
//            return file.getAbsolutePath();
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
