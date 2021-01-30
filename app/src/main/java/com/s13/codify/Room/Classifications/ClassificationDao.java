package com.s13.codify.Room.Classifications;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.s13.codify.Room.Images.Images;

import java.util.Date;
import java.util.List;

@Dao
public interface ClassificationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Classification classification);

    @Query("DELETE FROM Classification")
    void deleteAll();

    @Query("SELECT classification_timestamp FROM Classification ORDER BY classification_timestamp DESC LIMIT 1")
    Date getLastClassifiedTimestamp();

    @Query("SELECT image_id FROM Classification WHERE classification = :label")
    List<String> getByImagesClassification(String label);

    @Query("SELECT image_id FROM Classification WHERE classification NOT IN (:labels)")
    List<String> getImagesWithStatusNotInList(List<String> labels);


}
