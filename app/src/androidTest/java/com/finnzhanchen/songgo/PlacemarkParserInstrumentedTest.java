package com.finnzhanchen.songgo;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PlacemarkParserInstrumentedTest {
    private XmlPlacemarkParser parser = new XmlPlacemarkParser();
    private String onePlacemarkKML =
                            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                            "<Document>\n"  +
                            "  <Placemark>\n" +
                            "    <name>34:1</name>\n" +
                            "    <description>boring</description>\n" +
                            "    <styleUrl>#boring</styleUrl>\n" +
                            "    <Point>\n" +
                            "      <coordinates>-3.1896948527782767,55.945066395849906,0</coordinates>\n" +
                            "    </Point>\n" +
                            "  </Placemark>\n" +
                            "</Document>\n" +
                            "</kml>";

    private String twoPlacemarkKML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
                    "<Document>\n"  +
                    "  <Placemark>\n" +
                    "    <name>34:1</name>\n" +
                    "    <description>boring</description>\n" +
                    "    <styleUrl>#boring</styleUrl>\n" +
                    "    <Point>\n" +
                    "      <coordinates>-3.1896948527782767,55.945066395849906,0</coordinates>\n" +
                    "    </Point>\n" +
                    "  </Placemark>\n" +
                    "  <Placemark>\n" +
                    "    <name>9:1</name>\n" +
                    "    <description>interesting</description>\n" +
                    "    <styleUrl>#interesting</styleUrl>\n" +
                    "    <Point>\n" +
                    "      <coordinates>-3.1846328883274992,55.945077798427945,0</coordinates>\n" +
                    "    </Point>\n" +
                    "  </Placemark>" +
                    "</Document>\n" +
                    "</kml>";

    private InputStream onePlacemarkXMLStream
            = new ByteArrayInputStream(onePlacemarkKML.getBytes());

    private InputStream twoPlacemarkXMLStream
            = new ByteArrayInputStream(twoPlacemarkKML.getBytes());

    @Test
    public void parseOnePlacemarkPositionTest() throws Exception{
        List<Placemark> placemarks = parser.parse(onePlacemarkXMLStream);
        assertEquals("34:1", placemarks.get(0).position);
    }

    @Test
    public void parseOnePlacemarkSizeTest() throws Exception {
        List<Placemark> placemarks = parser.parse(onePlacemarkXMLStream);
        assertEquals(1, placemarks.size());
    }

    @Test
    public void parseOnePlacemarkUrlStyleTest() throws Exception{
        List<Placemark> placemarks = parser.parse(onePlacemarkXMLStream);
        assertEquals("#boring", placemarks.get(0).styleUrl);
    }

    @Test
    public void parseOnePlacemarkDescriptionTest() throws Exception{
        List<Placemark> placemarks = parser.parse(onePlacemarkXMLStream);
        assertEquals("boring", placemarks.get(0).description);
    }

    @Test
    public void parseOnePlacemarkPointTest() throws Exception{
        List<Placemark> placemarks = parser.parse(onePlacemarkXMLStream);
        LatLng expectedLatLng = new LatLng( 55.945066395849906,-3.1896948527782767);
        assertEquals(expectedLatLng.latitude, placemarks.get(0).point.latitude, 0.0000000001);
        assertEquals(expectedLatLng.longitude, placemarks.get(0).point.longitude, 0.000000001);
    }

    @Test
    public void parseTwoPlacemarkSizeTest() throws Exception{
        List<Placemark> placemarks = parser.parse(twoPlacemarkXMLStream);
        assertEquals(2, placemarks.size());
    }




}
