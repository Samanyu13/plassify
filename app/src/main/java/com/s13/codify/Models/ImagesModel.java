package com.s13.codify.Models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.s13.codify.Room.Images.Images;
import com.s13.codify.Room.Images.ImagesRepo;

import java.util.List;

import static com.s13.codify.Utils.Constants.IMAGE_STATUS_MEME;

public class ImagesModel extends AndroidViewModel {

    private ImagesRepo imageRepository;

    private List<String> images;

    public ImagesModel (Application application) {
        super(application);
        imageRepository = new ImagesRepo(application);
        images = imageRepository.getAllImagesByImageStatus(IMAGE_STATUS_MEME);
    }

    List<String> getAllImages() { return images; }

    public void insert(Images image) { imageRepository.insert(image); }
}
