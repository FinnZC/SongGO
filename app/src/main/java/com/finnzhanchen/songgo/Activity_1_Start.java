package com.finnzhanchen.songgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_1_Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_start_);
    }

    /** Called when the user taps the Start button */
    public void buttonStartGame(View view) {
        Intent intent = new Intent(this, Activity_2_Choose_Song.class);
        startActivity(intent);
        
    }

    /** Called when the user taps the Tutorial button */
    public void buttonTutorial(View view) {
        Intent intent = new Intent(this,Activity_3_Game.class);
        startActivity(intent);

    }
}
