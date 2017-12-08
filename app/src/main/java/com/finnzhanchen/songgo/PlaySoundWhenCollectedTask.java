package com.finnzhanchen.songgo;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

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