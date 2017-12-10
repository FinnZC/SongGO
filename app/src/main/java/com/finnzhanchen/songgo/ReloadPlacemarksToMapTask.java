package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.concurrent.ConcurrentHashMap;

// WRITTEN BY ME: FINN ZHAN CHEN

public class ReloadPlacemarksToMapTask extends AsyncTask<Void, Void, Void> {

    GoogleMap map;
    ConcurrentHashMap<Marker, Placemark> markerMap;

    public ReloadPlacemarksToMapTask(GoogleMap map, ConcurrentHashMap<Marker, Placemark> markerMap) {
        this.map = map;
        this.markerMap = markerMap;
    }

    @Override
    protected Void doInBackground(Void... fileName) {
        return null;
    }

    @Override
    protected void onPostExecute(Void a) {
        Marker marker = null;
        // Reestablish marker reference to placemarks on map and re-plot markers
        for (Placemark placemark : markerMap.values()) {
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
        }
    }
}