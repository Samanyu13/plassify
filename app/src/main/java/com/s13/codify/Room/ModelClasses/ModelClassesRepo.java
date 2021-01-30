package com.s13.codify.Room.ModelClasses;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.s13.codify.Room.ImagesRoomDatabase;
import java.util.List;



public class ModelClassesRepo {

    private ModelClassesDao modelClassesDao;

    public ModelClassesRepo(Application application) {
        ImagesRoomDatabase db = ImagesRoomDatabase.getDatabase(application);
        modelClassesDao = db.modelClassesDao();
    }


    public LiveData<List<ModelClass>> getModelClassByPreference(boolean selected){
        LiveData<List<ModelClass>> modelClasses = modelClassesDao.getModelClassByPreference(selected);
        return modelClasses;
    }

    public LiveData<List<ModelClass>> getModelClasses(){
        return modelClassesDao.getModelClasses();
    }

    public List<String> getListModelClassByPreference(boolean selected){
        return modelClassesDao.getListofModelClassWithPreference(selected);
    }

    public void deleteModelClasses(){
        new deleteAsyncTask(modelClassesDao).execute();
    }


    public void insert(ModelClass modelClass) {
        new insertAsyncTask(modelClassesDao).execute(modelClass);
    }


    public void updatePreference(ModelClass modelClass){
        new updateAsyncTask(modelClassesDao).execute(modelClass);
    }


    private static class insertAsyncTask extends AsyncTask<ModelClass, Void, Void> {

        private ModelClassesDao mAsyncTaskDao;

        insertAsyncTask(ModelClassesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ModelClass... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<ModelClass, Void, Void> {

        private ModelClassesDao mAsyncTaskDao;

        deleteAsyncTask(ModelClassesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ModelClass... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<ModelClass, Void, Void>{
        private ModelClassesDao mAsyncTaskDao;

        updateAsyncTask(ModelClassesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ModelClass... params) {
            mAsyncTaskDao.updateModelClass(params[0].isSelected(),params[0].getClassName());
            return null;
        }
    }


}
