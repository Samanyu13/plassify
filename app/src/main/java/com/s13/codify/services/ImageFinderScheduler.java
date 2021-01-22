package com.s13.codify.services;

import android.app.job.JobInfo.Builder;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ImageFinderScheduler extends JobService {
    private static final String TAG = "SyncService";
    public static final int SECONDS = 1000;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        System.out.println("SCHEDULER SAYS HELLO SOMA");
        //Schedule finder
        Context context = getApplicationContext();
        Intent imageFinder = new Intent(getApplicationContext(), ImageFinder.class);
        context.startService(imageFinder);
        // Schedule classifier
        Intent imageClassifier = new Intent(getApplicationContext(), ImageClassifier.class);
        context.startService(imageClassifier);

        ComponentName serviceComponent = new ComponentName(this, ImageFinderScheduler.class);
        Builder builder = new Builder(0, serviceComponent);
        builder.setMinimumLatency(30 * SECONDS);
        builder.setOverrideDeadline(30 * SECONDS);
        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        System.out.println("SCHEDULER SAYS BYE SOMA");
        return true;
    }
}
