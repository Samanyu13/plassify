package com.s13.codify.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

@Entity(tableName = "Images")

public class Images {

    @Ignore
    public Images(@NonNull String imagePath) {
        this.setImagePath(imagePath);
    }

    public Images(@NonNull String imagePath, String imageStatus) {
        this.setImagePath(imagePath);
        this.setImageStatus(imageStatus);
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "image_status",defaultValue = IMAGE_STATUS_NOT_CHECKED)
    private String imageStatus;

    @ColumnInfo(name = "last_classified_timestamp")
    private Date timestamp;

    @NonNull
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
