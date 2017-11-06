package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class DownloadLyricsTask extends AsyncTask<String, Void, InputStream> {

    ArrayList<ArrayList<String>> lyrics = new ArrayList<ArrayList<String>>();
    public DownloadLyricsTask(ArrayList<ArrayList<String>> lyrics){
        this.lyrics = lyrics;
    }

    // CODE FROM UNIVERSITY OF EDINBURGH SOFTWARE ENGINEERING LARGE PRACTICAL LECTURE 4
    @Override
    protected InputStream doInBackground(String... urls) {
        try {
            return downloadUrl(urls[0]);
        } catch (IOException e) {
            Log.e("Enter", "Entered exception");
            return null;
        }
    }

    // WRITTEN BY ME: FINN ZHAN CHEN
    @Override
    protected void onPostExecute(InputStream result) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(result));
        String line;
        try {
            while ((line = reader.readLine()) != null) {


            }
        }
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
}
