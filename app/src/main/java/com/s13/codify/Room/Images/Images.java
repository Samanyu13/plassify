package com.s13.codify.Room.Images;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_NOT_CHECKED;

@Entity(tableName = "Images")

public class Images {

    public Images(){};

    @Ignore
    public Images(@NonNull String imagePath) {
        this.setImagePath(imagePath);
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "image_path")
    private String imagePath;


    @NonNull
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(@NonNull String imagePath) {
        this.imagePath = imagePath;
    }

}
