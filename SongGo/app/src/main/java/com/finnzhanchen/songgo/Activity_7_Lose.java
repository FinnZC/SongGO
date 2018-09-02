package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_7_Lose extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_7_lose);

        // Reset the remaining guesses and superpower to 0 after losing
        // without accumulating for the next game
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("guess_remaining", 0);
        editor.putInt("superpower_remaining", 0);
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
