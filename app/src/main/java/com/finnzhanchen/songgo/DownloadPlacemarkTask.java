package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


//SOFWARE ENGINEERLING LARGE PRACTICAL LECTURE 4
//WHERE MY OWN CODE STARTS IS DOCUMENTED


public class DownloadPlacemarkTask extends AsyncTask<String, Void, List<Placemark>> {

    GoogleMap map;

    public DownloadPlacemarkTask(GoogleMap map){
        this.map = map;
    }

    @Override
    protected List<Placemark> doInBackground(String... urls) {
        try {

            return loadXmlFromNetwork(urls[0]);
        } catch (IOException e) {
            Log.e("Enter", "Entered exception");
            return null;
        } catch (XmlPullParserException e) {
            Log.e("Enter", "Entered exception");
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Placemark> result) {
        // Do something with result
        for (Placemark placemark: result){
            //Log.e("Placemark", placemark.description + " " + placemark.position + " " + placemark.styleUrl);
            switch (placemark.styleUrl){
                case "#veryinteresting":
                    map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.description)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.very_interesting)));
                    break;
                case "#interesting":
                    map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.description)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.interesting)));
                    break;
                case "#notboring":
                    map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.description)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notboring)));
                    break;
                case "#boring":
                    map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.description)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.boring)));
                    break;
                case "#unclassified":
                    map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.description)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.unclassified)));
                    break;
                default:
                    break;
            }
        }
    }

    private List<Placemark> loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks;
        Log.e("Enter", "Entered load");
        try (InputStream stream = downloadUrl(urlString)){
            XmlPlacemarkParser parser = new XmlPlacemarkParser();
            placemarks = parser.parse(stream);
        }
        return placemarks;
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
