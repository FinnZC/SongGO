package com.finnzhanchen.songgo;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class SongUnitTest {
    @Test
    public void oneSongTest() throws Exception {
        Song song = new Song("01", "Queen", "Bohemian Rhapsody", "youtube.com");
        assertEquals("01", song.number);
        assertEquals("Queen", song.artist);
        assertEquals("Bohemian Rhapsody", song.title);
        assertEquals("youtube.com", song.link);
    }
}