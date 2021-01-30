package com.s13.codify.Room.ModelClasses;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ModelClassesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ModelClass modelClass);

    @Query("DELETE FROM model_classes")
    void deleteAll();

    @Query("SELECT * FROM model_classes where selected = :selected")
    LiveData<List<ModelClass>> getModelClassByPreference(boolean selected);

    @Query("SELECT * FROM model_classes")
    LiveData<List<ModelClass>> getModelClasses();

    @Query("UPDATE model_classes SET selected = :selected where class_name = :className")
    void updateModelClass(boolean selected, String className);

    @Query("SELECT class_name FROM model_classes where selected = :selected")
    List<String> getListofModelClassWithPreference(boolean selected);
}
