package com.example.myfirstapp.Utillities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.example.myfirstapp.R;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SoundExecutor {
    private Context context;
    private Executor executor;
    private Handler handler;
    private MediaPlayer mediaPlayerSuccess;
    private MediaPlayer mediaPlayerFail;


    public SoundExecutor(Context context) {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void playSoundSuccess() {
        mediaPlayerSuccess = MediaPlayer.create(context, R.raw.succsess);
        mediaPlayerSuccess.setLooping(false);
        mediaPlayerSuccess.setVolume(1.0f, 1.0f);
        mediaPlayerSuccess.start();
    }

    public void stopSoundSuccess() {
        if(mediaPlayerSuccess != null) {
            executor.execute(() -> {
                mediaPlayerSuccess.stop();
                mediaPlayerSuccess.release();
                mediaPlayerSuccess = null;
            });
        }
    }

    public void playSoundFail() {
        mediaPlayerFail = MediaPlayer.create(context, R.raw.fail);
        mediaPlayerFail.setLooping(false);
        mediaPlayerFail.setVolume(1.0f, 1.0f);
        mediaPlayerFail.start();
    }

    public void stopSoundFail() {
        if(mediaPlayerFail != null) {
            executor.execute(() -> {
                mediaPlayerFail.stop();
                mediaPlayerFail.release();
                mediaPlayerFail = null;
            });
        }
    }
}
