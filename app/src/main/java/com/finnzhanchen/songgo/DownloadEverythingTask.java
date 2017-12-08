package com.finnzhanchen.songgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class DownloadEverythingTask extends AsyncTask<String, Void, Void> {
    Activity callingActivity = new Activity();
    ProgressDialog dialog = null;

    public DownloadEverythingTask(Activity callingActivity){
        this.callingActivity = callingActivity;
        this.dialog =new ProgressDialog(callingActivity);
    }

    @Override
    protected void onPreExecute(){
        // Download progress dialog
        dialog.setMessage("Please wait until all maps have been downloaded.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(String... urls) {
        if (!isSameVersion()){
            try {
                // Get a list of all available songs and download them to internal storage.
                List<Song> songs = loadXmlSongsFromNetwork(urls[0]);
                downloadAllSongsToInternalStorage(songs);
            } catch (IOException|XmlPullParserException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void nothing) {
        SharedPreferences settings = callingActivity.getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("map_exists",true);
        editor.apply();
        dialog.hide();
    }


    private boolean isSameVersion(){
        String xmlPath = callingActivity.getFilesDir() + "/SongsXML";
        File xmlFile = new File(xmlPath);
        // Check if song.xml exists in the internal storage
        if (xmlFile.exists() && !xmlFile.isDirectory()){
            try {
                BufferedReader localSongXML = new BufferedReader(new FileReader(xmlFile));
                URL songURL = new URL("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml");
                BufferedReader onlineSongXML = new BufferedReader(new InputStreamReader(songURL.openStream()));
                // Ignore the first line which is "<?xml version="1.0" encoding="UTF-8"?>"
                localSongXML.readLine();
                onlineSongXML.readLine();
                // The second time includes the timestamp
                if (localSongXML.readLine().equals(onlineSongXML.readLine())){
                    Log.e("Is same version?", "YES!");
                    // Same timestamp so return true.
                    return true;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        Log.e("Is same version?", "NO!");
        return false;
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




    private void downloadAllSongsToInternalStorage(List<Song> songsList){
        String url;
        String outputFileName;
        for (Song song : songsList){
            for (int i = 1; i <= 5 ; i++){
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"
                        + song.number + "/map" + i + ".kml";
                // Output file names are of format "Song1-Map1"
                outputFileName = "Song" + song.number + "-Map" + i;
                saveFile(url, outputFileName);


                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"
                        + song.number + "/words.txt";
                outputFileName = "Lyrics" + song.number;
                saveFile(url, outputFileName);
                Log.e("Stage downloadAllSongs", "Song: " + song.number +"Map: " + i);
                Log.e("Stage downloadAllSongs", "Lyrics: " + song.number);
            }
        }
        // Save SongsXMl last because this way ensures all songs are downloaded correctly
        // during isSameVersion check
        saveFile("http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml"
                , "SongsXML");
    }

    private void saveFile(String url, String outputFileName){
        try {
            // Name of the output file inputStream the url, literally saving a copy of the url in
            // internal storage
            InputStream inputStream = downloadUrl(url);
            File outputFile = new File(callingActivity.getFilesDir(), outputFileName);
            Log.e("Path of saved output", outputFile.getPath());
            FileOutputStream outputStream = callingActivity.openFileOutput(outputFileName, Context.MODE_PRIVATE);
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
