package com.finnzhanchen.songgo;

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

public class XmlParser {
    // We don’t use namespaces
    private static final String ns = null;

    public class Placemark {
        public final String position; // For example, 48:3
        public final String description;
        public final String styleUrl;
        public final LatLng point;

        Placemark(String position, String description, String styleUrl, LatLng point) {
            this.position = position;
            this.description = description;
            this.styleUrl = styleUrl;
            this.point = point;
        }
    }

    public List<Placemark> parse(InputStream in) throws XmlPullParserException, IOException{
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readDocument(parser);
        } finally {
            in.close();
        }
    }

    private List<Placemark> readDocument(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        List<Placemark> placemarks = new ArrayList<Placemark>();
        parser.require(XmlPullParser.START_TAG, ns, "Document");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("Placemark")) {
                Placemark placemark = readPlacemark(parser);
                System.out.println(placemark.toString());
                placemarks.add(placemark);

            } else {
                skip(parser);
            }
        }
        return placemarks;
    }

    private Placemark readPlacemark(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Placemark");
        String position = null; String description = null; String styleUrl = null; LatLng point = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            if (name.equals("name")) {
                position = readPosition(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("styleUrl")) {
                styleUrl = readStyleUrl(parser);
            } else if (name.equals("Point")) {
                point = readPoint(parser);
            }else {
                skip(parser);
            }
        }
        return new Placemark(position, description, styleUrl, point);
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

    private LatLng readPoint(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Point");
        if (parser.getEventType() != XmlPullParser.START_TAG)
            return null;
        String name = parser.getName();
        if (name.equals("coordinates")) {
            return readCoordinates(parser);
        } else {
            skip(parser);
        }
        return null;
    }

    private LatLng readCoordinates(XmlPullParser parser) throws IOException,
            XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "coordinates");
        String[] coordinates = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG, ns, "coordinates");
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
        return new LatLng(lat, lng);
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
