package com.s13.codify.Room.Classifications;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.s13.codify.Room.ImagesRoomDatabase;

import java.util.List;

public class ClassificationRepo {

    ClassificationDao classificationDao;

    public ClassificationRepo(Application application) {
        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(application);
        classificationDao = db.classificationDao();
    }

    public List<String> getByImagesClassification(String label){
        return classificationDao.getByImagesClassification(label);
    }

    public List<String> getImagesWithStatusNotInList(List<String> labels){
        return classificationDao.getImagesWithStatusNotInList(labels);
    }

}
