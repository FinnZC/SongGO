package com.finnzhanchen.songgo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class DownloadSongTask extends AsyncTask<String, Void, List<Song>> {
    Activity callingActivity = new Activity();
    public DownloadSongTask(Activity callingActivity){
        this.callingActivity = callingActivity;
    }

    @Override
    protected List<Song> doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            return null;
        } catch (XmlPullParserException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Song> result) {
        Spinner spinner_song = (Spinner) callingActivity.findViewById(R.id.spinner_song);
        // Spinner Drop down elements
        List<String> songs = new ArrayList<>();
        for (Song song : result){
            //Log.i("Songs detail",song.number + " " + song.title + " " + song.artist + " " + song.link);
            songs.add(song.number/* + " " + song.title + " " + song.artist + " " + song.link*/);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_song =
                new ArrayAdapter<String>(callingActivity,
                        android.R.layout.simple_spinner_item, songs);
        // Drop down layout style - list view with radio button
        dataAdapter_song.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_song.setAdapter(dataAdapter_song);

        // Spinner element for difficulty
        Spinner spinner_difficulty = (Spinner) callingActivity.findViewById(R.id.spinner_difficulty);
        // Spinner Drop down elements
        String[] difficulties = new String[] {"Novice", "Easy", "Normal", "Hard", "Hardcore"};
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_difficulty = new ArrayAdapter<String>(
                callingActivity, android.R.layout.simple_spinner_item, difficulties);
        dataAdapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner_difficulty.setAdapter(dataAdapter_difficulty);
    }

    private List<Song> loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        List<Song> songs;
        try (InputStream stream = downloadUrl(urlString)){
            // Do something with stream e.g. parse as XML, build result
            XmlSongParser parser = new XmlSongParser();
            songs = parser.parse(stream);
        }
        return songs;
    }

    // Given a string representation of a URL, sets up a connection and gets
    // an input stream.
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
