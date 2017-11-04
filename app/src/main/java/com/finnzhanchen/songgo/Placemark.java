package com.finnzhanchen.songgo;
import com.google.android.gms.maps.model.LatLng;
/**
 * Created by chen on 02/11/2017.
 */

public class Placemark {
    public final String position; // For example, 48:3
    public final String description;
    public final String styleUrl;
    public final LatLng point;

    public Placemark(String position, String description, String styleUrl, LatLng point) {
        this.position = position;
        this.description = description;
        this.styleUrl = styleUrl;
        this.point = point;
    }
}

