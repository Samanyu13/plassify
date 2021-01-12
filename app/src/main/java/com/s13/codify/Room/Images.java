package com.s13.codify.Room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

@Entity(tableName = "Images")

public class Images {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "imagePath")
    private String imagePath;

    @Ignore
    public Images(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

    public Images(@NonNull String imagePath, String imageStatus) {
        this.imagePath = imagePath;
        this.imageStatus=imageStatus;
    }

    public String getImagePath() {
        return imagePath;
    }


    @ColumnInfo(name = "imageStatus",defaultValue = IMAGE_STATUS_NOT_CHECKED)
    private String imageStatus;


    public String getImageStatus() {
        return imageStatus;
    }
}
