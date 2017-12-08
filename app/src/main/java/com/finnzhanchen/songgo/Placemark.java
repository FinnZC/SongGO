package com.finnzhanchen.songgo;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by chen on 02/11/2017.
 */
// WRITTEN BY ME: FINN ZHAN CHEN
    // Serializable so it can be passed as extra message in intent
public class Placemark implements Serializable {
    public final String position; // For example, 48:3
    public final String description;
    public final String styleUrl;
    public LatLng point;

    public Placemark(String position, String description, String styleUrl, LatLng point) {
        this.position = position;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
    }
}

