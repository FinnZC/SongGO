package com.finnzhanchen.songgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Activity_2_Choose_Song extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_choose_song);

        // Spinner element for songs
        Spinner spinner_song = (Spinner) findViewById(R.id.spinner_song);

        // Spinner Drop down elements
        List<String> songs = new ArrayList<>();

        songs.add("Song 1");
        songs.add("Dance with me | Novice");
        songs.add("Song 3");
        songs.add("Umbrella | Harcore");
        songs.add("Song 5");
        songs.add("Song 6");
        songs.add("Song 7");
        songs.add("Song 8");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_song = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, songs);

        // Drop down layout style - list view with radio button
        dataAdapter_song.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_song.setAdapter(dataAdapter_song);



        // Spinner element for difficulty
        Spinner spinner_difficulty = (Spinner) findViewById(R.id.spinner_difficulty);

        // Spinner Drop down elements
        List<String> difficulties = new ArrayList<>();

        difficulties.add("Novice");
        difficulties.add("Easy");
        difficulties.add("Normal");
        difficulties.add("Hard");
        difficulties.add("Hardcore");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_difficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficulties);
        dataAdapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_difficulty.setAdapter(dataAdapter_difficulty);

    }
}
