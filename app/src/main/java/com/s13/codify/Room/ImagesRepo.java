package com.s13.codify.Room;

//package com.example.memedetection.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

//import com.example.memedetection.Room.ImageDao;
//import com.example.memedetection.Room.ImageRoomDataset;
//import com.example.memedetection.Room.Images;

import java.util.List;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImagesRepo {

    private ImagesDao imagesDao;
    private LiveData<List<Images>> allImagesByImageStatus;



    public ImagesRepo(Application application) {
        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(application);
        imagesDao = db.imagesDao();
//        allNotCheckedImages = imagesDao.getNotCheckedImageList("not_checked");

    }

    public List<Images> getAllImagesByImagesList(List<String> imagePathList) {
        return imagesDao.getImageDataByImagePath(imagePathList);
    }

    public LiveData<List<Images>> getAllImagesByImageStatus(String queryStatus) {
        allImagesByImageStatus = imagesDao.getImageListByImageStatus(queryStatus);

        return allImagesByImageStatus;
    }

    public LiveData<List<Images>> getAllImagesExceptQueryStatus(String queryStatus) {
        return imagesDao.getImageListByNotImageStatus(queryStatus);
    }

    public List<Images> getImageListByImageStatusNotCheck() {
        return imagesDao.getImageListByImageStatusNotCheck(IMAGE_STATUS_NOT_CHECKED);
    }

    public void deleteImagesByImagePath(String path){
        imagesDao.deleteByImagePath(path);
    }


    public void insert(Images word) {
        new insertAsyncTask(imagesDao).execute(word);
    }

    public void deleteByStatus(String status){

        new deleteAsyncTask(imagesDao).execute(status);

//        mImageDao.deleteByStatus(status);
    }

    public void updateLabelByImagePath(String imagePath, String label){
        imagesDao.updateImageLabelByImagePath(imagePath,label);
    }

    public int getRowCount() {
        return imagesDao.getRowCount();
    }

    private static class insertAsyncTask extends AsyncTask<Images, Void, Void> {

        private ImagesDao mAsyncTaskDao;

        insertAsyncTask(ImagesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Images... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<String, Void, Void> {

        private ImagesDao mAsyncTaskDao;

        deleteAsyncTask(ImagesDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.deleteByStatus(params[0]);
            return null;
        }
    }
}
