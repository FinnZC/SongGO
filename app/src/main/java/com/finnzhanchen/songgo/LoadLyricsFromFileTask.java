package com.finnzhanchen.songgo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;



// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class LoadLyricsFromFileTask extends AsyncTask<String, Void, Void> {
    Activity callingActivity = new Activity();
    // key is the line number and the values are the words on that line
    HashMap<String, String[]> lyrics;

    public LoadLyricsFromFileTask(Activity callingActivity, HashMap<String, String[]> lyrics){
        this.callingActivity = callingActivity;
        this.lyrics = lyrics;
    }

    // CODE FROM UNIVERSITY OF EDINBURGH SOFTWARE ENGINEERING LARGE PRACTICAL LECTURE 4
    @Override
    protected Void doInBackground(String... fileName) {
        // Parse kml file from internal storage
        String lyricPath = callingActivity.getFilesDir() + "/" + fileName[0];
        Log.e("Lyric path:", lyricPath);
        File file = new File(lyricPath);

        try {
            InputStream stream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null){
                addLineLyrics(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        // print out lyrics hashmap for debugging
        for (String item : lyrics.keySet()) {
            Log.e("Lyrics", item + " " + Arrays.toString(lyrics.get(item)));
        }
        */
        Log.e("Loading Lyrics", "Completed");
        return null;
    }


    private void addLineLyrics(String line){
        // One line in the lyrics file provided has the following format
        // "     1\tIs this the real life?"
        // Splits line by delimiter \t
        // Returns string of format ["     01", "My song lyrics"]
        String[] lineSplit = line.split("\\t");

        //Log.e("Line splitted: ", Arrays.toString(lineSplit));

        // Remove unnecessary preceding spaces for line number
        String line_number = lineSplit[0].replaceAll("\\s", "");

        // Splits string of words by delimiters
        // Special case when the line has no words, thus lineSplit.length = 1
        if (lineSplit.length == 2) {
            String[] line_words = lineSplit[1].split(" ");
            //Log.e("Line Number: ", line_number + " Words: " + Arrays.toString(line_words));
            lyrics.put(line_number, line_words);
        }

    }
}
