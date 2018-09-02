package com.finnzhanchen.songgo;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SongParserInstrumentedTest {
    private XmlSongParser parser = new XmlSongParser();
    private String oneSongXML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Songs timestamp=\"2017-12-10T17:35:49.405Z[Europe/London]\"\n" +
                    "       root=\"http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/\">\n" +
                    "  <Song>\n" +
                    "    <Number>01</Number>\n" +
                    "    <Artist>Queen</Artist>\n" +
                    "    <Title>Bohemian Rhapsody</Title>\n" +
                    "    <Link>https://youtu.be/fJ9rUzIMcZQ</Link>\n" +
                    "  </Song>\n" +
                    "</Songs>";

    private String twoSongXML =
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<Songs timestamp=\"2017-12-10T17:35:49.405Z[Europe/London]\"\n" +
                    "       root=\"http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/\">\n" +
                    "  <Song>\n" +
                    "    <Number>01</Number>\n" +
                    "    <Artist>Queen</Artist>\n" +
                    "    <Title>Bohemian Rhapsody</Title>\n" +
                    "    <Link>https://youtu.be/fJ9rUzIMcZQ</Link>\n" +
                    "  </Song>\n" +
                    "  <Song>\n" +
                    "    <Number>02</Number>\n" +
                    "    <Artist>Blur</Artist>\n" +
                    "    <Title>Song 2</Title>\n" +
                    "    <Link>https://youtu.be/SSbBvKaM6sk</Link>\n" +
                    "  </Song>\n" +
                    "</Songs>";

    private InputStream oneSongXMLStream
            = new ByteArrayInputStream(oneSongXML.getBytes());

    private InputStream twoSongXMLStream
            = new ByteArrayInputStream(twoSongXML.getBytes());

    @Test
    public void parseOneSongSizeTest() throws Exception {
        List<Song> songs = parser.parse(oneSongXMLStream);
        assertEquals(1, songs.size());
    }

    @Test
    public void parseOneSongNumberTest() throws Exception {
        List<Song> songs = parser.parse(oneSongXMLStream);
        assertEquals("01", songs.get(0).number);
    }

    @Test
    public void parseOneSongArtistTest() throws Exception {
        List<Song> songs = parser.parse(oneSongXMLStream);
        assertEquals("Queen", songs.get(0).artist);
    }

    @Test
    public void parseOneSongTitleTest() throws Exception {
        List<Song> songs = parser.parse(oneSongXMLStream);
        assertEquals("Bohemian Rhapsody", songs.get(0).title);
    }

    @Test
    public void parseOneSongLinkTest() throws Exception {
        List<Song> songs = parser.parse(oneSongXMLStream);
        assertEquals("https://youtu.be/fJ9rUzIMcZQ", songs.get(0).link);
    }

    @Test
    public void parseTwoSongSizeTest() throws Exception {
        List<Song> songs = parser.parse(twoSongXMLStream);
        assertEquals(2, songs.size());
    }

    @Test
    public void parseTwoSongNumberTest() throws Exception {
        List<Song> songs = parser.parse(twoSongXMLStream);
        assertEquals("01", songs.get(0).number);
        assertEquals("02", songs.get(1).number);
    }

    @Test
    public void parseTwoSongArtistTest() throws Exception {
        List<Song> songs = parser.parse(twoSongXMLStream);
        assertEquals("Queen", songs.get(0).artist);
        assertEquals("Blur", songs.get(1).artist);
    }

    @Test
    public void parseTwoSongTitleTest() throws Exception {
        List<Song> songs = parser.parse(twoSongXMLStream);
        assertEquals("Bohemian Rhapsody", songs.get(0).title);
        assertEquals("Song 2", songs.get(1).title);
    }

    @Test
    public void parseTwoSongLinkTest() throws Exception {
        List<Song> songs = parser.parse(twoSongXMLStream);
        assertEquals("https://youtu.be/fJ9rUzIMcZQ", songs.get(0).link);
        assertEquals("https://youtu.be/SSbBvKaM6sk", songs.get(1).link);
    }
}
