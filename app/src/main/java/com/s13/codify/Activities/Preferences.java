package com.s13.codify.Activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.s13.codify.Adapters.PreferencesAdapter;
import com.s13.codify.Models.Row;
import com.s13.codify.R;


public class Preferences extends AppCompatActivity {
    List<Row> rows;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        listView = (ListView) findViewById(android.R.id.list);


        rows = new ArrayList<Row>(30);
        Row row = null;
        for (int i = 1; i < 31; i++) {
            row = new Row();
            row.setTitle("Title " + i);
            rows.add(row);
        }

        rows.get(3).setChecked(true);
        rows.get(6).setChecked(true);
        rows.get(9).setChecked(true);

        listView.setAdapter(new PreferencesAdapter(this, rows));

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Preferences.this,
                        rows.get(position).getTitle(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}