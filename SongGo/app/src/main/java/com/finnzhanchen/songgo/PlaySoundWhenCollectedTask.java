package com.finnzhanchen.songgo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;

// WRITTEN BY ME: FINN ZHAN CHEN

public class PlaySoundWhenCollectedTask extends AsyncTask<Void, Void, Void> {
    Activity callingActivity = new Activity();

    public PlaySoundWhenCollectedTask(Activity callingActivity){
        this.callingActivity = callingActivity;
    }
    @Override
    protected Void doInBackground(Void... fileName) {
        final MediaPlayer mp = MediaPlayer.create(callingActivity, R.raw.sound);
        mp.start();
        return null;
    }
}