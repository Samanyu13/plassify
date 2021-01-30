package com.s13.codify.Room.Classifications;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.s13.codify.Room.Images.Images;

import java.util.Date;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

@Entity(tableName = "Classification",
        foreignKeys = @ForeignKey(
                entity = Images.class, parentColumns = "image_path", childColumns = "image_id", onDelete = ForeignKey.CASCADE
        ))
public class Classification {

    public Classification(String imageId, String status, Date timestamp){
        this.imageId = imageId;
        this.status = status;
        this.timestamp = timestamp;
    }

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "image_id")
    private String imageId;

    @ColumnInfo(name = "classification", defaultValue = IMAGE_STATUS_NOT_CHECKED)
    private String status;

    @ColumnInfo(name = "classification_timestamp")
    private Date timestamp;


    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
