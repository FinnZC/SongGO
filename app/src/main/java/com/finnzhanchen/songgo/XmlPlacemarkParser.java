package com.finnzhanchen.songgo;

import android.util.Log;
import android.util.Xml;

import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 02/11/2017.
 */

// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED
public class XmlPlacemarkParser {
    private static final String ns = null;

    public List<Placemark> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            //Log.e("Stream", "parse stage 1 okay");
            return readKml(parser);
        } finally {
            in.close();
        }
    }


    private List<Placemark> readKml(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        //Log.e("Kml", "Reached");
        List<Placemark> placemarks = new ArrayList<Placemark>();
        parser.require(XmlPullParser.START_TAG, ns, "kml");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Document")) {
                placemarks = readDocument(parser);
            } else {
                skip(parser);
            }
        }
        return placemarks;
    }

    private List<Placemark> readDocument(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        //Log.e("Doc", "Reached");
        List<Placemark> placemarks = new ArrayList<Placemark>();
        parser.require(XmlPullParser.START_TAG, ns, "Document");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Log.e("Parse Tag in readDoc", name);
            // Starts by looking for the entry tag
            if (name.equals("Placemark")) {
                Placemark placemark = readPlacemark(parser);
                //Log.e("Placemark", placemark.position + " " + placemark.styleUrl + " "
                //    + placemark.description);
                placemarks.add(placemark);

            } else {
                skip(parser);
            }
        }
        return placemarks;
    }


    private Placemark readPlacemark(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        //Log.e("Placemark", "Reached");
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");
        String position = null;
        String description = null;
        String styleUrl = null;
        LatLng point = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            //Log.e("Parse Tag in Placemark:", name);
            if (name.equals("name")) {
                position = readPosition(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("styleUrl")) {
                styleUrl = readStyleUrl(parser);
            } else if (name.equals("Point")) {
                point = readPoint(parser);
            } else {
                skip(parser);
            }
        }
        return new Placemark(position, description, styleUrl, point);
    }

    private LatLng readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        //Log.e("Point", "Reached");
        LatLng placemark = null;
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Log.e("Parse Tag in readPoint", name);
            // Starts by looking for the entry tag
            if (name.equals("coordinates")) {
                placemark = readCoordinates(parser);

            } else {
                skip(parser);
            }
        }
        return placemark;
    }

    private LatLng readCoordinates(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        //Log.e("Coord", "Reached");
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String[] coordinates = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        double lng = Double.parseDouble(coordinates[0]);
        double lat = Double.parseDouble(coordinates[1]);
        //Log.e("coordinates", lat + " " + lng);
        return new LatLng(lat, lng);
    }

    private String readPosition(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String position = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return position;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readStyleUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "styleUrl");
        String styleUrl = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "styleUrl");
        return styleUrl;
    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
