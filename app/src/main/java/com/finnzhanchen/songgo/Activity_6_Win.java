package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_6_Win extends AppCompatActivity {
    private int guessRemaining;
    private int superpowerRemaining;
    private Song songSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_win);
        guessRemaining = getIntent().getIntExtra("guessRemaining", 0);
        superpowerRemaining = getIntent().getIntExtra("superpowerRemaining", 0);
        songSelected = (Song) getIntent().getSerializableExtra("songSelected");

        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        TextView clearedTextView = (TextView) findViewById(R.id.textView_you_cleared);
        String name = settings.getString("user_name", "" /*Default value */);
        String text = name + " you cleared:";
        clearedTextView.setText(text);

        TextView songDetailTextView = (TextView) findViewById(R.id.textView_win_song);
        text = songSelected.title + " by " + songSelected.artist;
        songDetailTextView.setText(text);

        TextView remainingTextView = (TextView) findViewById(R.id.textView_remaining);
        text = guessRemaining + " guesses and "
                + superpowerRemaining + " superpower accumulated for next game!";
        remainingTextView.setText(text);


        // Saves the remaining guesses and superpower for next game
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("guess_remaining", guessRemaining);
        editor.putInt("superpower_remaining", superpowerRemaining);
        editor.apply();
    }

    /** Called when the user taps the PlayMore button */
    public void buttonPlayMore(View view) {
        Intent intent = new Intent(this, Activity_1_Start.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Disable back button
    }
}
