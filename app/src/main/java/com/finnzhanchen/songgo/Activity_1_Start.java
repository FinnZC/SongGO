package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Activity_1_Start extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_start_);

        // Get previous user name if exist
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        String name = settings.getString("user_name", "" /*Default*/);
        nameBox.setText(name);

    }

    /** Called when the user taps the Start button */
    public void buttonStartGame(View view) {
        // Save user name in SharedPreferences
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("mystring", "wahay");
        editor.putString("user_name", nameBox.getText().toString());
        editor.commit();

        Intent intent = new Intent(this, Activity_2_Choose_Song.class);
        startActivity(intent);
        
    }

    /** Called when the user taps the Tutorial button */
    public void buttonAchievement(View view) {
        Intent intent = new Intent(this,Activity_7_Achievement.class);
        startActivity(intent);

    }
}
