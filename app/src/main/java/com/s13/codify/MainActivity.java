package com.s13.codify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataList = findViewById(R.id.dataList);

        titles = new ArrayList<>();
        images = new ArrayList<>();

        for(int i = 0; i < (ModelClasses.MODEL_CLASSES).length; i++) {
            titles.add(ModelClasses.MODEL_CLASSES[i]);
        }

        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);
        images.add(R.drawable.ic_baseline_folder_24);

        adapter = new Adapter(this,titles,images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);
    }
}