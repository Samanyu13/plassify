package com.s13.codify.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.s13.codify.R;

public class Activity2 extends Activity {

    Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
    }

}