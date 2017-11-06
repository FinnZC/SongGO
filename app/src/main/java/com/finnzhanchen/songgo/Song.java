package com.finnzhanchen.songgo;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by chen on 02/11/2017.
 */
// WRITTEN BY ME: FINN ZHAN CHEN
    // Serializable is used so that a song can be passed as extra in Intent
public class Song implements Serializable {
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

