package com.finnzhanchen.songgo;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class DownloadXmlTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        try {
            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            return "Unable to load content. Check your network connection";
        } catch (XmlPullParserException e) {
            return "Error parsing XML";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // Do something with result
    }

    private String loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        StringBuilder result = new StringBuilder();
        try (InputStream stream = downloadUrl(urlString)){
            // Do something with stream e.g. parse as XML, build result
            XmlParser parser = new XmlParser();
            List<Placemark> placemarks = parser.parse(stream);
        }
        return result.toString();
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
