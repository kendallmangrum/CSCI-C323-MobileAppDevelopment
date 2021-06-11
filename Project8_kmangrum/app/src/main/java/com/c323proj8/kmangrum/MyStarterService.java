package com.c323proj8.kmangrum;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

//  Create service for background music
public class MyStarterService extends Service {


    private final IBinder myBinder = new LocalServiceBinder();

//    Create a new thread and start it
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread thread = new Thread(new MyThread());
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class LocalServiceBinder extends Binder {
        MyStarterService getService() {
            return MyStarterService.this;
        }
    }


//    Have thread extend runnable
    final class MyThread implements Runnable {

//        When ran. play music and keep it looping
        @Override
        public void run() {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.chimes);
            mp.setLooping(true);
            mp.start();
        }

        
    }
}
