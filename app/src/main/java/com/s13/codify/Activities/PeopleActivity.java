package com.s13.codify.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.s13.codify.Models.People;
import com.s13.codify.Adapters.PeopleAdapter;
import com.s13.codify.R;

import java.util.ArrayList;
import java.util.List;

public class PeopleActivity extends AppCompatActivity {

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