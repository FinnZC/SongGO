package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class Activity_1_Start extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_start_);

        // Get previous user name if exist
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        String name = settings.getString("user_name", "" /*Default value */);
        nameBox.setText(name);

        if (Connectivity.isConnectedWifi(this) || Connectivity.isConnectedMobile(this)){
            String song_url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";
            new DownloadEverythingTask(this).execute(song_url);
        } else{
            Log.e("Connectivity:", "No WiFi nor 4G detected. Using maps from internal" +
                    "storage");
            // Telling users that no internet connection is detected so the game will use
            // downloaded maps. If no maps are downloaded, then user cannot select any map.
            Context context = getApplicationContext();
            String text = "No WiFi nor 4G detected. Using maps from internal storage.If no map" +
                    "is downloaded then you cannot select a map to play :(";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /** Called when the user taps the Start button */
    public void buttonStartGame(View view) {
        // Save user name in SharedPreferences
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user_name", nameBox.getText().toString());
        editor.apply();


        //Check if there are any maps downloaded and ready for play
        Boolean map_exists = settings.getBoolean("map_exists", false /*Default value */);

        if (map_exists){
            Intent intent = new Intent(this, Activity_2_Choose_Song.class);
            startActivity(intent);
        } else {
            // Telling user that there are no maps to play
            Context context = getApplicationContext();
            String text = "There are no maps to play. Please connect to the Internet!";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /** Called when the user taps the Tutorial button */
    public void buttonAchievement(View view) {
        Intent intent = new Intent(this,Activity_8_Achievement.class);
        startActivity(intent);
    }
}
