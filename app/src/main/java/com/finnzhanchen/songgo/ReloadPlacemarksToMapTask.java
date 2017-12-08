package com.finnzhanchen.songgo;

import android.os.AsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;

// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class ReloadPlacemarksToMapTask extends AsyncTask<Void, Void, Void> {

    GoogleMap map;
    HashMap<Marker, Placemark> markerMap;

    public ReloadPlacemarksToMapTask(GoogleMap map, HashMap<Marker, Placemark> markerMap) {
        this.map = map;
        this.markerMap = markerMap;
    }

    @Override
    protected Void doInBackground(Void... fileName) {
        return null;
    }

    @Override
    protected void onPostExecute(Void a){
        map.clear();
        for (Placemark placemark : markerMap.values()) {
            switch (placemark.styleUrl) {
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
}