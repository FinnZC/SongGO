package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Activity_5_Guess_Song extends AppCompatActivity {
    private int guessRemaining;
    private int superpowerRemaining;
    private Song songSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_guess_song);
        guessRemaining = getIntent().getIntExtra("guessRemaining", 0);
        superpowerRemaining = getIntent().getIntExtra("superpowerRemaining", 0);
        songSelected = (Song) getIntent().getSerializableExtra("songSelected");
        updateText();
    }

    /** Called when the user taps the Submit button */
    public void buttonSubmit(View view) {
        // Get user guess song
        TextView textBoxInputSong = (TextView) findViewById(R.id.text_input_song);
        String song = textBoxInputSong.getText().toString();
        // Test number of guess and superpower
        Log.e("SUPERPOWER, GUESS: ", superpowerRemaining + " " + guessRemaining);

        if (songSelected.title.equals(song)){
            // WIN!
            Intent intent = new Intent(this, Activity_6_Win.class);
            // Clear Activity Stack so user cannot go back to the game when win
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("guessRemaining", guessRemaining);
            intent.putExtra("superpowerRemaining", superpowerRemaining);
            intent.putExtra("songSelected", songSelected);
            addSongToAchievement();
            startActivity(intent);
        } else{
            // LOST!
            guessRemaining = guessRemaining - 1;
            if (guessRemaining > 0) {
                Context context = getApplicationContext();
                String text = "WRONG! " + guessRemaining + " GUESSES REMAINING!";
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                toast.show();
                updateText();
            } else {
                Intent intent = new Intent(this, Activity_7_Lose.class);
                // Clear Activity Stack so user cannot go back to the game when lost
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Saves the remaining guesses and powerpower for next game
                SharedPreferences settings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("guess_remaining", 0);
                editor.putInt("superpower_remaining", 0);
                editor.apply();
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("guessRemaining", guessRemaining);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateText(){
        TextView textBox = (TextView) findViewById(R.id.text_guess_remaining);
        String text = "Guess Remaining: " + guessRemaining;
        textBox.setText(text);
    }

    private void addSongToAchievement(){
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        // Use HashSet to store the completed songs
        Set<String> completedSongsSet = settings.getStringSet("completed_songs", new HashSet<String>());
        // Get date
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String date = dateFormat.format(new Date()); //2017/11/16 12:08
        // Get where the map was played
        String where = getIntent().getStringExtra("where");
        // Get user name
        String user_name = settings.getString("user_name", "No Name");
        // Get difficulty
        String difficulty = getIntent().getStringExtra("difficulty");
        // Get song name and author
        String songDetails = songSelected.title + " by " + songSelected.artist;
        // Combine info and add formatted string to completedSongsSet for further processing later
        String item = date + "\n"
                + where + "\n"
                + user_name + " - " + difficulty + "\n"
                + songDetails + "\n"
                + "CLICK TO LISTEN ON YOUTUBE"
                + "," /* special symbol for formatting*/ + songSelected.link;
        completedSongsSet.add(item);

        // Update the saved set of completed songs in SharedPreferences setting
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet("completed_songs", completedSongsSet);
        editor.apply();

    }

}
