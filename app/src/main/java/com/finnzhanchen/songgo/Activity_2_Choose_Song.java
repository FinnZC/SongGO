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
        String where_selected = ((Spinner)
                findViewById(R.id.spinner_where)).getSelectedItem().toString();
        String song_number_selected = ((Spinner)
                findViewById(R.id.spinner_song)).getSelectedItem().toString();
        String difficulty_selected = ((Spinner)
                findViewById(R.id.spinner_difficulty)).getSelectedItem().toString();

        Song song_selected = songMap.get(song_number_selected);
        intent.putExtra("where_selected", where_selected);
        intent.putExtra("songSelected", song_selected);
        intent.putExtra("difficulty_selected", difficulty_selected);

        startActivity(intent);
    }

    /** Called when the user taps the GO button */
    public void buttonRandom(View view) {
        Intent intent = new Intent(this, Activity_3_Game.class);
        String where_selected = ((Spinner)
                findViewById(R.id.spinner_where)).getSelectedItem().toString();

        Random rand = new Random();
        Spinner song_selector = (Spinner) findViewById(R.id.spinner_song);
        // Get random number between 0 to max number of songs
        int x = rand.nextInt(song_selector.getCount());
        Song song_selected = songMap.get(song_selector.getItemAtPosition(x).toString());
        //Log.e("Random Song: ", x + "");
        //Log.e("Random Song Selected: ", songSelected);

        // Get random number between 0 to 4 which represent from Novice to Hardcore
        x = rand.nextInt(5);
        String difficulty_selected = ((Spinner)
                findViewById(R.id.spinner_difficulty)).getItemAtPosition(x).toString();

        Log.e("Random Difficulty: ", x + "");
        Log.e("Difficulty Selected: ", difficulty_selected);

        intent.putExtra("where_selected", where_selected);
        intent.putExtra("songSelected", song_selected);
        intent.putExtra("difficulty_selected", difficulty_selected);

        startActivity(intent);
    }
}

