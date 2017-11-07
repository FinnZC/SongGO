package com.finnzhanchen.songgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity_6_Win extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_6_win);

    }

    /** Called when the user taps the PlayMore button */
    public void buttonPlayMore(View view) {
        Intent intent = new Intent(this, Activity_7_Lose.class);
        startActivity(intent);
    }
}
