package com.finnzhanchen.songgo;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by chen on 02/11/2017.
 */

public class Song {
    public final String number; // For example, 48:3
    public final String artist;
    public final String title;
    public final String link;

    public Song(String number, String artist, String title, String link) {
        this.number = number;
        this.artist = artist;
        this.title = title;
        this.link = link;
    }
}

