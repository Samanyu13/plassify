package com.s13.codify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.s13.codify.Activities.ImagesDisplayActivity;
import com.s13.codify.Activities.Preferences;
import com.s13.codify.Adapters.Adapter;
import com.s13.codify.Models.ModelClasses;
import com.s13.codify.Room.ModelClasses.ModelClass;
import com.s13.codify.Room.ModelClasses.ModelClassesRepo;
import com.s13.codify.services.ImageFinderScheduler;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    RecyclerView dataList;
    List<String> titles;
    List<Integer> images;
    Adapter adapter;
    ModelClassesRepo repo;
    private static final int MY_READ_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingSupportFragment
        if (!sharedPreferences.getBoolean(
                getString(R.string.pref_previously_started), false)) {

            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            edit.commit();
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            startActivity(new Intent(this, Preferences.class));
        }


        FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("test").build();
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        System.out.println("Model Downloaded");
                    }
                });

        //Check permission
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        }
        repo = new ModelClassesRepo(getApplication());
        runImageFinder(this);


        setContentView(R.layout.activity_main);
        dataList = findViewById(R.id.dataList);

        titles = new ArrayList<>();
        images = new ArrayList<>();


        adapter = new Adapter(this,titles,images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        dataList.setLayoutManager(gridLayoutManager);
        dataList.setAdapter(adapter);

        LiveData<List<ModelClass>> selectedModelClasses = repo.getModelClassByPreference(true);
        selectedModelClasses.observe(this, new Observer<List<ModelClass>>() {
            @Override
            public void onChanged(List<ModelClass> modelClasses) {
                List<String> updatedTitles = new ArrayList<>();
                List<Integer> updatedImages = new ArrayList<>();
                for(ModelClass modelClass: modelClasses){
                    updatedTitles.add(modelClass.getClassName());
                    updatedImages.add(R.drawable.ic_baseline_folder_24);
                }
                updatedTitles.add("Others");
                updatedImages.add(R.drawable.ic_baseline_folder_24);
                adapter.setImage(updatedImages);
                adapter.setTitles(updatedTitles);
                dataList.setAdapter(adapter);

            }
        });

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if(exit) {
            finish();
        }
        else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    private void runImageFinder(Context context){
        // This runs the job in the main thread.
        ComponentName serviceComponent = new ComponentName(context, ImageFinderScheduler.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000); // wait at least
        builder.setOverrideDeadline(2 * 1000); // maximum delay
        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
            case R.id.action_settings:
//                Toast.makeText(getApplicationContext(),"Settings Click",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Preferences.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
//        return true;
    }

}