package com.finnzhanchen.songgo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class DownloadEverythingTask extends AsyncTask<String, Void, List<Song>> {
    Activity callingActivity = new Activity();
    HashMap<String, Song> songMap = new HashMap<String, Song>();

    public DownloadEverythingTask(Activity callingActivity, HashMap<String, Song> songMap){
        this.callingActivity = callingActivity;
        this.songMap = songMap;
    }

    @Override
    protected List<Song> doInBackground(String... urls) {
        List<Song> songs = new ArrayList<Song>();
        try {
            songs = loadXmlSongsFromNetwork(urls[0]);
        } catch (IOException|XmlPullParserException e) {
            e.printStackTrace();
        }
        return songs;
    }

    @Override
    protected void onPostExecute(List<Song> songsList) {
        updateUserInterfaceSpinners(songsList);
        downloadAllSongsToInternalStorage(songsList);

    }

    private List<Song> loadXmlSongsFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        List<Song> songs = new ArrayList<Song>();
        try (InputStream stream = downloadUrl(urlString)){
            // Do something with stream e.g. parse as XML, build result
            XmlSongParser parser = new XmlSongParser();
            songs = parser.parse(stream);
        }
        return songs;
    }

    private void updateUserInterfaceSpinners(List<Song> result) {
        Spinner spinner_song = (Spinner) callingActivity.findViewById(R.id.spinner_song);
        // Spinner Drop down elements
        List<String> song_spinner_data = new ArrayList<>();
        for (Song song : result){
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

    private void downloadAllSongsToInternalStorage(List<Song> songsList){
        for (Song song : songsList){
            for (int i = 1; i <= 5 ; i++){
                String url = String.format("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/%d/map%d.kml", song.number, i);
                try {
                    // Name of the output file inputStream the url, literally saving a copy of the url in
                    // internal storage
                    InputStream inputStream = downloadUrl(url);
                    FileOutputStream outputStream = callingActivity.openFileOutput(url, Context.MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    //read from inputStream to buffer
                    while((bytesRead = inputStream.read(buffer)) !=-1){
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    //flush OutputStream to write any buffered data to file
                    outputStream.flush();
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }
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
