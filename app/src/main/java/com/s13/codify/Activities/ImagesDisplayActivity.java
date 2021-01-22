package com.s13.codify.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s13.codify.Adapters.GalleryAdapter;
import com.s13.codify.Models.ImageGallery;
import com.s13.codify.R;
import com.s13.codify.Room.Images;
import com.s13.codify.Room.ImagesRepo;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class ImagesDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GalleryAdapter galleryAdapter;
    List<String> images;
    TextView gallery_number;

    private static final int MY_READ_PERMISSION_CODE = 101;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_image_display);

        gallery_number = findViewById(R.id.gallery_number);
        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        loadImages();

    }

    private void loadImages() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        String modelClass = getIntent().getStringExtra("MODELCLASS");

        ImagesRepo repo = new ImagesRepo(getApplication());
        LiveData<List<String>> imagesLive = repo.getAllImagesByImageStatus(modelClass);
        imagesLive.observe(this, new Observer<List<String>>()  {
            @Override
            public void onChanged(List<String> images) {
                if(images == null)
                    images = new ArrayList<>();
                galleryAdapter.setImages(images);
            }
        });

         galleryAdapter = new GalleryAdapter(this, images, new GalleryAdapter.PhotoListener() {
            @Override
            public void onPhotoClick(String path) {
                // Do something with the photo here
                Toast.makeText(ImagesDisplayActivity.this, ""+path, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(galleryAdapter);
        gallery_number.setText("Photos (0)");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_READ_PERMISSION_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read external storage permission granted..!", Toast.LENGTH_SHORT).show();
                loadImages();
            }
            else {
                Toast.makeText(this, "Read External Permission denied..:/", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
