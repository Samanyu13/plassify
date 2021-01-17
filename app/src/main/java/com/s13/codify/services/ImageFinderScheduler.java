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
        Context context = getApplicationContext();
        Intent service = new Intent(getApplicationContext(), ImageFinder.class);
        context.startService(service);
        ComponentName serviceComponent = new ComponentName(this, ImageFinderScheduler.class);
        Builder builder = new Builder(0, serviceComponent);
        builder.setMinimumLatency(60 * SECONDS); // wait at least
        builder.setOverrideDeadline(60 * SECONDS); // maximum delay
        JobScheduler jobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(builder.build());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
