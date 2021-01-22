package com.s13.codify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.s13.codify.R;

import java.util.List;

//import com.s13.codify.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewGalleryHolder> {

    private Context context;
    private List<String> images;
    protected PhotoListener photoListener;

    public GalleryAdapter(Context context, List<String> images, PhotoListener photoListener) {
        this.context = context;
        this.images = images;
        this.photoListener = photoListener;
    }

    @NonNull
    @Override
    public ViewGalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewGalleryHolder(
                LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGalleryHolder holder, int position) {
        String image = images.get(position);

        Glide.with(context).load(image).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListener.onPhotoClick(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    public class ViewGalleryHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ViewGalleryHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);

        }
    }

    public interface PhotoListener {
        void onPhotoClick(String path);
    }

    public void setImages(List<String> images){
        this.images = images;
    }
}
