package com.finnzhanchen.songgo;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlacemarkUnitTest {
    @Test
    public void onePlacemarkTest() throws Exception {
        Placemark placemark = new Placemark("40:2", "interesting", "#interesting", new LatLng(1, 10));
        assertEquals("40:2", placemark.position);
        assertEquals("interesting", placemark.description);
        assertEquals("#interesting", placemark.styleUrl);
        assertEquals(1, (int) placemark.point.latitude);
        assertEquals(10, (int) placemark.point.longitude);
    }

    @Test
    public void adjustPlacemarkPointTest(){
        LatLng adjustment = new LatLng(5,5);

        List<Placemark> placemarks = new ArrayList<>();
        placemarks.add(new Placemark("40:2", "interesting", "#interesting", new LatLng(3, 7)));
        placemarks.add(new Placemark("40:2", "interesting", "#interesting", new LatLng(5, 5)));

        for (Placemark placemark : placemarks){
            placemark.point = new LatLng(
                    placemark.point.latitude + adjustment.latitude,
                    placemark.point.longitude + adjustment.longitude);
        }

        assertEquals(3 + 5, (int) placemarks.get(0).point.latitude);
        assertEquals(7 + 5, (int) placemarks.get(0).point.longitude);
        assertEquals(5 + 5, (int) placemarks.get(1).point.latitude);
        assertEquals(5 + 5, (int) placemarks.get(1).point.longitude);
    }
}