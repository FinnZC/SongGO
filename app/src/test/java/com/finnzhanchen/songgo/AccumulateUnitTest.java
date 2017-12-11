package com.finnzhanchen.songgo;

import android.content.SharedPreferences;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class AccumulateUnitTest {
    @Test
    public void defaultGuessAccumlateTest() throws Exception {
        SharedPreferences setting = mock(SharedPreferences.class);
        int startingGuess = 5;
        when(setting.getInt("remainingGuess", 0)).thenReturn(0);
        assertEquals(startingGuess, setting.getInt("remainingGuess", 0) + startingGuess);
    }

    @Test
    public void pastVictoryGuessAccumlateTest() throws Exception {
        SharedPreferences setting = mock(SharedPreferences.class);
        int startingGuess = 5;
        // 3 guesses accumulated from past victory
        when(setting.getInt("remainingGuess", 0)).thenReturn(3);
        assertEquals(5 + 3, setting.getInt("remainingGuess", 0) + startingGuess);
    }

    @Test
    public void defaultSuperpowerAccumlateTest() throws Exception {
        SharedPreferences setting = mock(SharedPreferences.class);
        int startingSuperpower = 3;
        when(setting.getInt("remainingGuess", 0)).thenReturn(0);
        assertEquals(startingSuperpower, setting.getInt("remainingGuess", 0) + startingSuperpower);
    }

    @Test
    public void pastVictorySuperpowerAccumlateTest() throws Exception {
        SharedPreferences setting = mock(SharedPreferences.class);
        int startingSuperpower = 3;
        // 2 guesses accumulated from past victory
        when(setting.getInt("remainingGuess", 0)).thenReturn(2);
        assertEquals(3 + 2, setting.getInt("remainingGuess", 0) + startingSuperpower);
    }

}