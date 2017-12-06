package com.finnzhanchen.songgo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;



// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class LoadPlacemarkTask extends AsyncTask<String, Void, List<Placemark>> {

    GoogleMap map;
    HashMap<Marker, Placemark> markerMap;
    Activity callingActivity = new Activity();

    public LoadPlacemarkTask(Activity callingActivity, GoogleMap map, HashMap<Marker, Placemark> markerMap){
        this.callingActivity = callingActivity;
        this.map = map;
        this.markerMap = markerMap;
    }


    @Override
    protected List<Placemark> doInBackground(String... fileName) {
        try {
            return loadPlacemarksFromInternalStorage(fileName[0]);
        } catch (XmlPullParserException|IOException e) {
            Log.e("Enter", "Entered exception");
        }
        return null;
    }

    // WRITTEN BY ME: FINN ZHAN CHEN
    @Override
    protected void onPostExecute(List<Placemark> placemarks) {
        Marker marker = null;
        if (placemarks != null) {
            for (Placemark placemark : placemarks) {
                //Log.e("Placemark", placemark.description + " " + placemark.position + " " + placemark.styleUrl);
                // Save Placemarks in a dictionary of Markers so the reference of the marker
                // is used to remove the marker on the map and also keeps a reference of other
                // important information

                switch (placemark.styleUrl) {
                    case "#veryinteresting":
                        marker = map.addMarker(new MarkerOptions()
                                .position(placemark.point)
                                .title(placemark.description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.very_interesting)));
                        break;
                    case "#interesting":
                        marker = map.addMarker(new MarkerOptions()
                                .position(placemark.point)
                                .title(placemark.description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.interesting)));
                        break;
                    case "#notboring":
                        marker = map.addMarker(new MarkerOptions()
                                .position(placemark.point)
                                .title(placemark.description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.notboring)));
                        break;
                    case "#boring":
                        marker = map.addMarker(new MarkerOptions()
                                .position(placemark.point)
                                .title(placemark.description)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.boring)));
                        break;
                    case "#unclassified":
                        marker = map.addMarker(new MarkerOptions()
                                .position(placemark.point)
                                .title(placemark.description)
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
    }
    // WRITTEN BY ME: FINN ZHAN CHEN
    private List<Placemark> loadPlacemarksFromInternalStorage(String fileName) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks;
        // Parse kml file from internal storage
        String kmlPath = callingActivity.getFilesDir() + "/" + fileName;
        File file = new File(kmlPath);

        try (InputStream stream =  new FileInputStream(file)){
            XmlPlacemarkParser parser = new XmlPlacemarkParser();
            placemarks = parser.parse(stream);
        }
        return placemarks;
    }

}
