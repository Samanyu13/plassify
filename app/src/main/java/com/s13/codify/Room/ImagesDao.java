package com.s13.codify.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Images image);

    @Query("DELETE FROM Images")
    void deleteAll();

    @Query("DELETE FROM Images WHERE image_status = :queryStatus")
    void deleteByStatus(String queryStatus);

    @Query("DELETE FROM Images WHERE image_path = :imagePathString")
    void deleteByImagePath(String imagePathString);

    @Query("SELECT * from Images WHERE image_status = :queryText LIMIT 5")
    LiveData<List<Images>> getImageListByImageStatus(String queryText);

    @Query("SELECT * from Images WHERE image_status != :queryText ")
    LiveData<List<Images>> getImageListByNotImageStatus(String queryText);

    @Query("SELECT * from Images WHERE image_status = :queryText LIMIT 100")
    List<Images> getImageListByImageStatusNotCheck(String queryText);


    @Query("SELECT * from Images WHERE image_path IN (:queryList)")
    List<Images> getImageDataByImagePath(List<String> queryList);

    @Query("SELECT COUNT(image_path) FROM Images")
    int getRowCount();


}
