package com.finnzhanchen.songgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Random;

public class Activity_2_Choose_Song extends AppCompatActivity {
    HashMap<String, Song> songMap = new HashMap<String, Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_choose_song);
        new LoadSongFromFileTask(this, songMap).execute();
    }

    /** Called when the user taps the GO button */
    public void buttonGO(View view) {
        Intent intent = new Intent(this, Activity_3_Game.class);
        String whereSelected = ((Spinner)
                findViewById(R.id.spinner_where)).getSelectedItem().toString();
        String songNumberSelected = ((Spinner)
                findViewById(R.id.spinner_song)).getSelectedItem().toString();
        String difficultySelected = ((Spinner)
                findViewById(R.id.spinner_difficulty)).getSelectedItem().toString();

        Song songSelected = songMap.get(songNumberSelected);
        intent.putExtra("whereSelected", whereSelected);
        intent.putExtra("songSelected", songSelected);
        intent.putExtra("difficultySelected", difficultySelected);

        startActivity(intent);
    }

    /** Called when the user taps the GO button */
    public void buttonRandom(View view) {
        Intent intent = new Intent(this, Activity_3_Game.class);
        String whereSelected = ((Spinner)
                findViewById(R.id.spinner_where)).getSelectedItem().toString();

        // Get random number between 0 to max number of songs
        Random rand = new Random();
        Spinner songSelector = (Spinner) findViewById(R.id.spinner_song);
        int x = rand.nextInt(songSelector.getCount());
        Song songSelected = songMap.get(songSelector.getItemAtPosition(x).toString());
        //Log.e("Random Song: ", x + "");

        // Get random number between 0 to 4 which represent from Novice to Hardcore
        x = rand.nextInt(5);
        String difficultySelected = ((Spinner)
                findViewById(R.id.spinner_difficulty)).getItemAtPosition(x).toString();
        Log.e("Difficulty Selected: ", difficultySelected);

        // Pass these objects to the next activity
        intent.putExtra("whereSelected", whereSelected);
        intent.putExtra("songSelected", songSelected);
        intent.putExtra("difficultySelected", difficultySelected);
        startActivity(intent);
    }

}

