package com.s13.codify.Room.Images;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.s13.codify.Room.Images.Images;

import java.util.Date;
import java.util.List;

@Dao
public interface ImagesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Images image);

    @Query("DELETE FROM Images")
    void deleteAll();

    @Query("DELETE FROM Images WHERE image_path = :imagePathString")
    void deleteByImagePath(String imagePathString);

    @Query("SELECT * from Images WHERE image_path IN (:queryList)")
    List<Images> getImageDataByImagePath(List<String> queryList);

    @Query("SELECT * from Images where image_path NOT IN ( SELECT DISTINCT image_id from Classification )")
    List<Images> getUnclassifiedImages();

    @Query("SELECT COUNT(image_path) FROM Images")
    int getRowCount();
}
