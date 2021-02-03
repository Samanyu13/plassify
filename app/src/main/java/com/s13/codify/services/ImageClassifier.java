//package com.s13.codify.services;
//
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.RectF;
//import android.os.IBinder;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
//import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
//import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
//import com.google.firebase.ml.custom.FirebaseModelInterpreter;
//import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
//import com.s13.codify.Models.ModelClasses;
//import com.s13.codify.Room.Classifications.Classification;
//import com.s13.codify.Room.Images.Images;
//import com.s13.codify.Room.ImagesRoomDatabase;
//import com.s13.codify.Room.ModelClasses.ModelClass;
//import com.s13.codify.Room.ModelClasses.ModelClassesRepo;
//import com.s13.codify.ml.Model;
//import com.s13.codify.ml.Y4Tiny;
//
////import org.pytorch.IValue;
////import org.pytorch.Module;
////import org.pytorch.Tensor;
////import org.pytorch.torchvision.TensorImageUtils;
//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.Interpreter;
//import org.tensorflow.lite.support.common.TensorOperator;
//import org.tensorflow.lite.support.common.TensorProcessor;
//import org.tensorflow.lite.support.common.ops.NormalizeOp;
//import org.tensorflow.lite.support.image.TensorImage;
//import org.tensorflow.lite.support.label.TensorLabel;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static com.s13.codify.Models.ModelClasses.MODEL_CLASSES;
//import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;
//import static com.s13.codify.Utils.Constants.MAX_RESULTS;
//
//public class ImageClassifier extends Service {
//    public static final int N_THREADS = 5;
//    public static boolean isRunning;
//    @Override
//    public void onCreate(){
//        super.onCreate();
//        isRunning = false;
//    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (!isRunning) {
//
////            FirebaseCustomRemoteModel remoteModel = new FirebaseCustomRemoteModel.Builder("test").build();
//            FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
//                    .setAssetFilePath("y4_test.tflite")
//                    .build();
//
////            FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
////                    .addOnCompleteListener(new OnCompleteListener<File>() {
////                        @Override
////                        public void onComplete(@NonNull Task<File> task) {
//                            insert_data();
////                            File modelFile = task.getResult();
////                            if (modelFile != null) {
//                                ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
//                                Context context = getApplicationContext();
//                                ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);
//                                executorService.execute(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        isRunning = true;
//                                        Interpreter interpreter = null;
////                                        Interpreter interpreter = new Interpreter(modelFile);
////                                        FirebaseModelInterpreter interpreter;
////                                        FirebaseModelInterpreterOptions options =
////                                                new FirebaseModelInterpreterOptions.Builder(localModel).build();
////                                        interpreter = FirebaseModelInterpreter.getInstance(options);
//
//                                        List<Images> images = db.imagesDao().getUnclassifiedImages();
//                                        for (Images image : images) {
//                                            String imagePath = image.getImagePath();
//                                            System.out.println("Classifying image.....");
//                                            String label = classify(imagePath, interpreter);
//                                            System.out.println("Classification complete !");
//                                            Date classificationTimestamp = new Date();
//                                            Classification classification = new Classification(imagePath, label, classificationTimestamp);
//                                            db.classificationDao().insert(classification);
//                                        }
//                                        isRunning = false;
//                                    }
//                                });
////                            }
////                        }
////                    });
//        }
//        return Service.START_NOT_STICKY;
//    }
//
//    public void insert_data(){
//        ModelClassesRepo repo = new ModelClassesRepo(getApplication());
//        String[] modelClasses = MODEL_CLASSES;
//        for(int i = 0; i < modelClasses.length ; i++){
//            ModelClass modelClass = new ModelClass();
//            modelClass.setClassName(modelClasses[i]);
//            modelClass.setSelected(false);
//            repo.insert(modelClass);
//        }
//    }
//
//    /** An immutable result returned by a Classifier describing what was recognized. */
//    public static class Recognition {
//        /**
//         * A unique identifier for what has been recognized. Specific to the class, not the instance of
//         * the object.
//         */
//        private final String id;
//
//        /** Display name for the recognition. */
//        private final String title;
//
//        /**
//         * A sortable score for how good the recognition is relative to others. Higher should be better.
//         */
//        private final Float confidence;
//
//        /** Optional location within the source image for the location of the recognized object. */
//        private RectF location;
//
//        public Recognition(
//                final String id, final String title, final Float confidence, final RectF location) {
//            this.id = id;
//            this.title = title;
//            this.confidence = confidence;
//            this.location = location;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public Float getConfidence() {
//            return confidence;
//        }
//
//        public RectF getLocation() {
//            return new RectF(location);
//        }
//
//        public void setLocation(RectF location) {
//            this.location = location;
//        }
//
//        @Override
//        public String toString() {
//            String resultString = "";
//            if (id != null) {
//                resultString += "[" + id + "] ";
//            }
//
//            if (title != null) {
//                resultString += title + " ";
//            }
//
//            if (confidence != null) {
//                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
//            }
//
//            if (location != null) {
//                resultString += location + " ";
//            }
//
//            return resultString.trim();
//        }
//    }
//
//
//    public String classify(String filePath, Interpreter interpreter) {
//                                //Read the image as Bitmap
//                                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
//
//                                //reshape the image into 400*400
//                                bitmap = Bitmap.createScaledBitmap(bitmap, 180, 180, true);
//
//
//                                // IP Image Buffer
////                                DataType imageDataType = interpreter.getInputTensor(0).dataType();
//                                TensorImage inputImageBuffer = new TensorImage(DataType.FLOAT32);
//                                inputImageBuffer.load(bitmap);
//
//                                //OP Image Buffer
////                                int[] probabilityShape = interpreter.getOutputTensor(0).shape();
////                                DataType probabilityDataType = interpreter.getOutputTensor(0).dataType();
////                                TensorBuffer outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
//
////                                interpreter.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
//
//                                try {
//                                    Model model = Model.newInstance(getApplicationContext());
//
//                                    // Creates inputs for reference.
//                                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 180 , 180, 3}, DataType.FLOAT32);
//                                    inputFeature0.loadBuffer(inputImageBuffer.getBuffer());
//
//                                    // Runs model inference and gets result.
//                                    Model.Outputs outputs = model.process(inputFeature0);
//                                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
////                                    TensorBuffer outputFeature1 = outputs.getOutputFeature1AsTensorBuffer();
//
//                                    // Releases model resources if no longer used.
////                                    model.close();
//                                    TensorProcessor probabilityProcessor;
//                                    probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();
//
//                                    List<String> labels = new ArrayList<>(Arrays.asList(ModelClasses.MODEL_CLASSES));
//
//                                    Map<String, Float> labeledProbability = new TensorLabel(labels, probabilityProcessor.process(outputFeature0))
//                                            .getMapWithFloatValue();
//
//                                    System.out.println("NANNAYI");
//                                } catch (IOException e) {
//                                    // TODO Handle the exception
//                                }
//
//                                // Creates the post processor for the output probability.
//                                ;
//
////                                for(Map.Entry<String, Float> xxx : labeledProbability.entrySet()) {
////                                    System.out.println(xxx.getKey() + ": KEY, VAL : " + xxx.getValue().toString());
////                                }
//
////                                Recognition kuttaappi = getTopKProbability(labeledProbability);
////
////                                return kuttaappi.getTitle();
//                                    return "OOOOMFI";
////                                System.out.println("SIVANE ITH ETH JILLA");
//                            }
//
//
//    /** Gets the top-k results. */
//    private static Recognition getTopKProbability(Map<String, Float> labelProb) {
//        // Find the best classifications.
//        PriorityQueue<Recognition> pq =
//                new PriorityQueue<>(
//                        MAX_RESULTS,
//                        new Comparator<Recognition>() {
//                            @Override
//                            public int compare(Recognition lhs, Recognition rhs) {
//                                // Intentionally reversed to put high confidence at the head of the queue.
//                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
//                            }
//                        });
//
//        for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
//            pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
//        }
//
//        Recognition recognition = pq.poll();
//        return recognition;
//    }
//
//    protected TensorOperator getPostprocessNormalizeOp() {
//        return new NormalizeOp(0.0f, 1.0f);
//    }
//
////    public static String fetchModelFile(Context context, String modelName) throws IOException {
////        File file = new File(context.getFilesDir(), modelName);
////        if (file.exists() && file.length() > 0) {
////            return file.getAbsolutePath();
////        }
////
////        try (InputStream is = context.getAssets().open(modelName)) {
////            try (OutputStream os = new FileOutputStream(file)) {
////                byte[] buffer = new byte[4 * 1024];
////                int read;
////                while ((read = is.read(buffer)) != -1) {
////                    os.write(buffer, 0, read);
////                }
////                os.flush();
////            }
////            return file.getAbsolutePath();
////        }
////    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}

package com.s13.codify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.IBinder;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.s13.codify.Models.Detector;
import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Models.TFLiteObjectDetectionAPIModel;
import com.s13.codify.Room.Classifications.Classification;
import com.s13.codify.Room.Images.Images;
import com.s13.codify.Room.ImagesRoomDatabase;
import com.s13.codify.Room.ModelClasses.ModelClass;
import com.s13.codify.Room.ModelClasses.ModelClassesRepo;

//import org.pytorch.IValue;
//import org.pytorch.Module;
//import org.pytorch.Tensor;
//import org.pytorch.torchvision.TensorImageUtils;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.s13.codify.Models.ModelClasses.MODEL_CLASSES;

public class ImageClassifier extends Service {
    public static final int N_THREADS = 5;
    public static boolean isRunning;
    private Detector detectors;

    // Configuration values for the prepackaged SSD model.
    private static final int TF_OD_API_INPUT_SIZE = 416;
    private static final boolean TF_OD_API_IS_QUANTIZED = false;
    private static final String MODEL_FILE = "yolov4-416.tflite";
    private static final String TF_OD_API_LABELS_FILE = "labelmap.txt";
    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.5f;
    private static final boolean MAINTAIN_ASPECT = false;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, 480);
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    private Matrix cropToFrameTransform;
    private Bitmap bitmap = null;



    @Override
    public void onCreate(){
        super.onCreate();
        isRunning = false;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {

            FirebaseCustomRemoteModel remoteModel = new FirebaseCustomRemoteModel.Builder("test").build();
            FirebaseModelManager.getInstance().getLatestModelFile(remoteModel)
                    .addOnCompleteListener(new OnCompleteListener<File>() {
                        @Override
                        public void onComplete(@NonNull Task<File> task) {
                            insert_data();
                            File modelFile = task.getResult();
                            if (modelFile != null) {
//                                interpreter = new Interpreter(modelFile);

                                System.out.println("XX");
                                String lala = modelFile.getPath();
                                System.out.println("XXX" + lala);
                                if (modelFile != null) {
                                    ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
                                    Context context = getApplicationContext();
                                    ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(context);
                                    executorService.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            isRunning = true;
                                            Interpreter interpreter = new Interpreter(modelFile);
                                            List<Images> images = db.imagesDao().getUnclassifiedImages();
                                            for (Images image : images) {

                                                String imagePath = image.getImagePath();
                                                System.out.println("Classifying image.....");
                                                List<String> labels = classify(imagePath, interpreter, lala);
                                                for(String label: labels){
                                                    System.out.println("Classification complete !");
                                                    Date classificationTimestamp = new Date();
                                                    Classification classification = new Classification(imagePath, label, classificationTimestamp);
                                                    db.classificationDao().insert(classification);
                                                }

                                            }

                                            isRunning = false;
                                        }
                                    });
                                }
                            }
                        }
                    });
        }
        return Service.START_NOT_STICKY;
    }

    public void insert_data(){
        ModelClassesRepo repo = new ModelClassesRepo(getApplication());
        String[] modelClasses = MODEL_CLASSES;
        for(int i = 0; i < modelClasses.length ; i++){
            ModelClass modelClass = new ModelClass();
            modelClass.setClassName(modelClasses[i]);
            modelClass.setSelected(false);
            repo.insert(modelClass);
        }
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    /** An immutable result returned by a Classifier describing what was recognized. */
//    public static class Recognition {
//        /**
//         * A unique identifier for what has been recognized. Specific to the class, not the instance of
//         * the object.
//         */
//        private final String id;
//
//        /** Display name for the recognition. */
//        private final String title;
//
//        /**
//         * A sortable score for how good the recognition is relative to others. Higher should be better.
//         */
//        private final Float confidence;
//
//        /** Optional location within the source image for the location of the recognized object. */
//        private RectF location;
//
//        public Recognition(
//                final String id, final String title, final Float confidence, final RectF location) {
//            this.id = id;
//            this.title = title;
//            this.confidence = confidence;
//            this.location = location;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public String getTitle() {
//            return title;
//        }
//
//        public Float getConfidence() {
//            return confidence;
//        }
//
//        public RectF getLocation() {
//            return new RectF(location);
//        }
//
//        public void setLocation(RectF location) {
//            this.location = location;
//        }
//
//        @Override
//        public String toString() {
//            String resultString = "";
//            if (id != null) {
//                resultString += "[" + id + "] ";
//            }
//
//            if (title != null) {
//                resultString += title + " ";
//            }
//
//            if (confidence != null) {
//                resultString += String.format("(%.1f%%) ", confidence * 100.0f);
//            }
//
//            if (location != null) {
//                resultString += location + " ";
//            }
//
//            return resultString.trim();
//        }
//    }


    public List<String> classify(String filePath, Interpreter interpreter, String modelPath) {

        int cropSize = TF_OD_API_INPUT_SIZE;
        try {
            detectors =
                    TFLiteObjectDetectionAPIModel.create(
                            this,
                            MODEL_FILE,
                            new ArrayList<>(Arrays.asList(ModelClasses.MODEL_CLASSES)),
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
//            cropSize = TF_OD_API_INPUT_SIZE;
        } catch (final IOException e) {
            e.printStackTrace();
//                                            LOGGER.e(e, "Exception initializing Detector!");
//            Toast toast =
//                    Toast.makeText(
//                            getApplicationContext(), "Detector could not be initialized", Toast.LENGTH_SHORT);
//            toast.show();
//            finish();
        }

        bitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888);


        bitmap = BitmapFactory.decodeFile(filePath);

        bitmap = Bitmap.createScaledBitmap(bitmap, 416, 416, true);

        List<String> labels = new ArrayList<String>();

        if(detectors != null) {
            final List<Detector.Recognition> results = detectors.recognizeImage(bitmap);
            for (int i = 0; i<results.size(); i++){
                Log.i("Member name: ", String.valueOf(results.get(i)));
            }
            for(int i = 0; i < results.size(); i++){
                labels.add(results.get(i).getTitle());
            }
        }



//        Bitmap cropCopyBitmap = Bitmap.createBitmap(bitmap);
//        final Canvas canvas = new Canvas(cropCopyBitmap);
//        final Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(2.0f);



//        final List<Detector.Recognition> mappedRecognitions =
//                new ArrayList<Detector.Recognition>();

//        for (final Detector.Recognition result : results) {
//            final RectF location = result.getLocation();
//            if (location != null && result.getConfidence() >= minimumConfidence) {
//                canvas.drawRect(location, paint);
//
//                cropToFrameTransform.mapRect(location);
//
//                result.setLocation(location);
//                mappedRecognitions.add(result);
//            }
//        }

//        // IP Image Buffer
//        DataType imageDataType = interpreter.getInputTensor(0).dataType();
//        TensorImage inputImageBuffer = new TensorImage(imageDataType);
//        inputImageBuffer.load(bitmap);
//
//        //OP Image Buffer
//        int[] probabilityShape = interpreter.getOutputTensor(0).shape();
//        DataType probabilityDataType = interpreter.getOutputTensor(0).dataType();
//        TensorBuffer outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
//
//        interpreter.run(inputImageBuffer.getBuffer(), outputProbabilityBuffer.getBuffer().rewind());
//
//
//        // Creates the post processor for the output probability.
//        ;
//        TensorProcessor probabilityProcessor;
//        probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();
//
//        List<String> labels = new ArrayList<>(Arrays.asList(ModelClasses.MODEL_CLASSES));
//
//        Map<String, Float> labeledProbability = new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
//                .getMapWithFloatValue();
//
////                                for(Map.Entry<String, Float> xxx : labeledProbability.entrySet()) {
////                                    System.out.println(xxx.getKey() + ": KEY, VAL : " + xxx.getValue().toString());
////                                }
//
//        Recognition kuttaappi = getTopKProbability(labeledProbability);

//        return kuttaappi.getTitle();
        return labels;
//                                System.out.println("SIVANE ITH ETH JILLA");
    }


    /** Gets the top-k results. */
//    private static Recognition getTopKProbability(Map<String, Float> labelProb) {
//        // Find the best classifications.
//        PriorityQueue<Recognition> pq =
//                new PriorityQueue<>(
//                        MAX_RESULTS,
//                        new Comparator<Recognition>() {
//                            @Override
//                            public int compare(Recognition lhs, Recognition rhs) {
//                                // Intentionally reversed to put high confidence at the head of the queue.
//                                return Float.compare(rhs.getConfidence(), lhs.getConfidence());
//                            }
//                        });
//
//        for (Map.Entry<String, Float> entry : labelProb.entrySet()) {
//            pq.add(new Recognition("" + entry.getKey(), entry.getKey(), entry.getValue(), null));
//        }
//
//        Recognition recognition = pq.poll();
//        return recognition;
//    }

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