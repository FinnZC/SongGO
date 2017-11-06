package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;



// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class DownloadPlacemarkTask extends AsyncTask<String, Void, List<Placemark>> {

    GoogleMap map;
    HashMap<Marker, Placemark> markerMap;

    public DownloadPlacemarkTask(GoogleMap map, HashMap<Marker, Placemark> markerMap){
        this.map = map;
        this.markerMap = markerMap;
    }

    // CODE FROM UNIVERSITY OF EDINBURGH SOFTWARE ENGINEERING LARGE PRACTICAL LECTURE 4
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

    // WRITTEN BY ME: FINN ZHAN CHEN
    @Override
    protected void onPostExecute(List<Placemark> result) {
        Marker marker = null;
        for (Placemark placemark: result){
            //Log.e("Placemark", placemark.description + " " + placemark.position + " " + placemark.styleUrl);
            // Save Placemarks in a dictionary of Markers so the reference of the marker
            // is used to remove the marker on the map and also keeps a reference of other
            // important information

            switch (placemark.styleUrl){
                case "#veryinteresting":
                    marker = map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.very_interesting)));
                    break;
                case "#interesting":
                    marker = map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.interesting)));
                    break;
                case "#notboring":
                    marker = map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.notboring)));
                    break;
                case "#boring":
                    marker = map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.boring)));
                    break;
                case "#unclassified":
                    marker = map.addMarker(new MarkerOptions()
                            .position(placemark.point)
                            .title(placemark.position)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.unclassified)));
                    break;
                default:
                    marker = null;
                    break;
            }
            if (marker != null && !markerMap.containsKey(marker)) {
                markerMap.put(marker, placemark);
            }

            /* Test that it actually removes the markers
            for (Marker mark: markerMap.keySet()){
                mark.remove();
            }
            */
        }
    }
    // WRITTEN BY ME: FINN ZHAN CHEN
    private List<Placemark> loadXmlFromNetwork(String urlString) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks;
        try (InputStream stream = downloadUrl(urlString)){
            XmlPlacemarkParser parser = new XmlPlacemarkParser();
            placemarks = parser.parse(stream);
        }
        return placemarks;
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
