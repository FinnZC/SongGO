package com.finnzhanchen.songgo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class LoadSongFromFileTask extends AsyncTask<Void, Void, List<Song>> {
    Activity callingActivity = new Activity();
    HashMap<String, Song> songMap = new HashMap<String, Song>();

    public LoadSongFromFileTask(Activity callingActivity, HashMap<String, Song> songMap){
        this.callingActivity = callingActivity;
        this.songMap = songMap;
    }

    @Override
    protected List<Song> doInBackground(Void... params) {
        try {
            return loadSongsFromInternalStorage();
        } catch (IOException|XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Song> songList) {
        if (songList != null) updateUserInterfaceSpinners(songList);
    }

    private List<Song> loadSongsFromInternalStorage() throws
            XmlPullParserException, IOException {
        List<Song> songs;
        String xmlPath = callingActivity.getFilesDir() + "/SongsXML";
        File file = new File( xmlPath );
        try (InputStream stream = new FileInputStream(file)){
            // Do something with stream e.g. parse as XML, build result
            XmlSongParser parser = new XmlSongParser();
            songs = parser.parse(stream);
        }
        return songs;
    }

    private void updateUserInterfaceSpinners(List<Song> songList) {
        Spinner spinner_song = (Spinner) callingActivity.findViewById(R.id.spinner_song);
        // Spinner Drop down elements
        List<String> song_spinner_data = new ArrayList<>();
        for (Song song : songList){
            song_spinner_data.add(song.number);
            /* + " " + song.title + " " + song.artist + " " + song.link*/
            // Save a hashmap for further processing later
            songMap.put(song.number, song);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_song =
                new ArrayAdapter<String>(callingActivity,
                        android.R.layout.simple_spinner_item, song_spinner_data);
        // Drop down layout style - list view with radio button
        dataAdapter_song.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_song.setAdapter(dataAdapter_song);

        // Spinner element for difficulty
        Spinner spinner_difficulty = (Spinner) callingActivity.findViewById(R.id.spinner_difficulty);
        // Spinner Drop down elements
        String[] difficulties = new String[] {"Novice", "Easy", "Normal", "Hard", "Extreme"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_difficulty = new ArrayAdapter<String>(
                callingActivity, android.R.layout.simple_spinner_item, difficulties);
        dataAdapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_difficulty.setAdapter(dataAdapter_difficulty);
    }
}
