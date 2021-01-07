package com.s13.codify.Activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.s13.codify.R;

public class IndividualPeopleActivity extends AppCompatActivity {

    private TextView iTitle, iDescription;
    private ImageView iImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        iTitle = (TextView) findViewById(R.id.textTitle);
        iDescription = (TextView) findViewById(R.id.txtDesc);
        iImg = (ImageView) findViewById(R.id.bookthumbnail);

        // Receive data
        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        int image = intent.getExtras().getInt("Thumbnail") ;

        // Setting values

        iTitle.setText(Title);
        iDescription.setText(Description);
        iImg.setImageResource(image);

    }
}