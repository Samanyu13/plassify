package com.s13.codify.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ImageFinder extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO : Find images
        System.out.println("SCHEDULER SAYS HELLO SOMA !");
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
