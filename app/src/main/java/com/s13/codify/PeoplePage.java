package com.s13.codify;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PeoplePage extends AppCompatActivity {

    List<People> lstBook ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_people);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lstBook = new ArrayList<>();

        for(int i = 0; i < 15; i++) {
            lstBook.add(new People("Person " + i,"Book Category x","Book Desc Y",R.drawable.thevigitarian));
        }

        RecyclerView myrv = (RecyclerView) findViewById(R.id.recyclerview_id);
        PeopleAdapter myAdapter = new PeopleAdapter(this,lstBook);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        myrv.setAdapter(myAdapter);


    }
}