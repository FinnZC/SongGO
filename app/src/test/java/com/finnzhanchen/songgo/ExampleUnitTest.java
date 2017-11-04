package com.finnzhanchen.songgo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void XmlTest() {
        String[] urls = new String[]{"http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/lyrics.txt"};
        DownloadPlacemarkTask d = new DownloadPlacemarkTask();
        d.doInBackground(urls);
        assertEquals(4,4);
    }

}