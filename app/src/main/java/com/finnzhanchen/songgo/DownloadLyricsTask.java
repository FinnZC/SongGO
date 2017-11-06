package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class DownloadLyricsTask extends AsyncTask<String, Void, Void> {
    // key is the line number and the values are the words on that line
    HashMap<String, String[]> lyrics;

    public DownloadLyricsTask(HashMap<String, String[]> lyrics){
        this.lyrics = lyrics;
    }

    // CODE FROM UNIVERSITY OF EDINBURGH SOFTWARE ENGINEERING LARGE PRACTICAL LECTURE 4
    @Override
    protected Void doInBackground(String... urls) {
        try {
            InputStream stream = downloadUrl(urls[0]);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null){
                addLineLyrics(line);
            }
            /*
            // print out lyrics hashmap for debugging
            for (String item : lyrics.keySet()) {
                Log.e("Lyrics", item + " " + Arrays.toString(lyrics.get(item)));
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // CODE FROM UNIVERSITY OF EDINBURGH SOFTWARE ENGINEERING LARGE PRACTICAL LECTURE 4
    // Given a string representation of a URL, sets up a connection and gets an input stream.
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // Also available: HttpsURLConnection
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }

    private void addLineLyrics(String line){
        // One line in the lyrics file provided has the following format
        // "     1\tIs this the real life?"
        // Splits line by delimiter \t
        // Returns string of format ["     01", "My song lyrics"]
        String[] lineSplitted = line.split("\\t");

        //Log.e("Line splitted: ", Arrays.toString(lineSplitted));

        // Remove unnecessary preceding spaces for line number
        String line_number = lineSplitted[0].replaceAll("\\s", "");

        // Splits string of words by delimiters
        // Special case when the line has no words, thus lineSplitteed.length = 1
        if (lineSplitted.length == 2) {
            String[] line_words = lineSplitted[1].split(" ");
            //Log.e("Line Number: ", line_number + " Words: " + Arrays.toString(line_words));
            lyrics.put(line_number, line_words);
        }

    }

    private  ArrayList<String>  filterArrayStringSpaces (String[] wordsUnfiltered){
        ArrayList<String>  wordsFiltered = new ArrayList<String>();
        for (String word : wordsUnfiltered){
            if (!word.equals("")) wordsFiltered.add(word);
        }
        return wordsFiltered;
    }
}
