package com.s13.codify.Room.Images;

//package com.example.memedetection.Repository;

import android.app.Application;
import android.os.AsyncTask;

import com.s13.codify.Room.Images.Images;
import com.s13.codify.Room.Images.ImagesDao;
import com.s13.codify.Room.ImagesRoomDatabase;

import java.util.Date;
//import com.example.memedetection.Room.ImageDao;
//import com.example.memedetection.Room.ImageRoomDataset;
//import com.example.memedetection.Room.Images;

import java.util.List;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

public class ImagesRepo {

    private ImagesDao imagesDao;
    private List<Images> allImagesByImageStatus;
    private List<String> allImagesPathByImageStatus;


    public ImagesRepo(Application application) {
        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(application);
        imagesDao = db.imagesDao();
    }

    public List<Images> getAllImagesByImagesList(List<String> imagePathList) {
        return imagesDao.getImageDataByImagePath(imagePathList);
    }

    public void deleteImagesByImagePath(String path){
        imagesDao.deleteByImagePath(path);
    }


    public void insert(Images word) {
        new insertAsyncTask(imagesDao).execute(word);
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

}
