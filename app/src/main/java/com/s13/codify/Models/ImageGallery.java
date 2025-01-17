package com.s13.codify.Models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;

public class ImageGallery {

    public static ArrayList<String> listOfImages(Context context) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage, folderName;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        cursor = context.getContentResolver().query(uri, projection, null,
                null, orderBy+" DESC");
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        // Get Folder Name
         column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            folderName = cursor.getString(column_index_folder_name);
            System.out.println(folderName);
            listOfAllImages.add(absolutePathOfImage);
        }

        return listOfAllImages;
    }
}
