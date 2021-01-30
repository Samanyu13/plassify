package com.s13.codify.Room.ModelClasses;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "model_classes")
public class ModelClass {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "class_name")
    private String className;

    @ColumnInfo(name = "selected", defaultValue = "false")
    private boolean selected;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
