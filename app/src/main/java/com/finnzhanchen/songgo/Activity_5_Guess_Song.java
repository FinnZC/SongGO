package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Activity_5_Guess_Song extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_guess_song);
    }

    /** Called when the user taps the Submit button */
    public void buttonSubmit(View view) {
        TextView textBoxInputSong = (TextView) findViewById(R.id.text_input_song);
        String song = textBoxInputSong.getText().toString();
        Song song_selected = (Song) getIntent().getSerializableExtra("song_selected");

        if (song_selected.title.equals(song)){
            Intent intent = new Intent(this, Activity_6_Win.class);
            startActivity(intent);
        } else{
            Context context = getApplicationContext();
            String text = "WRONG! 4 GUESSES REMAINING!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();

        }

    }
}
