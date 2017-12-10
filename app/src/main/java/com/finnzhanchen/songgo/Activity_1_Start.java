package com.finnzhanchen.songgo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_1_Start extends AppCompatActivity {
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_start_);
        // Initialise settings
        settings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);

        // Get previous user name if exist
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        String name = settings.getString("user_name", "" /*Default value */);
        nameBox.setText(name);

        // Get Location permission first
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Nothing
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        // Checks connectivity for downloading (version checks occur inside the download task)
        if (Connectivity.isConnectedWifi(this) || Connectivity.isConnectedMobile(this)){
            String song_url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml";
            new DownloadEverythingTask(this).execute(song_url);
        } else{
            Log.e("Connectivity:", "No WiFi nor 4G detected. Using maps from internal storage.");
            // Tell users that no internet connection is detected so the game will use downloaded maps.
            Context context = getApplicationContext();
            String text = "No WiFi nor 4G detected. Using downloaded maps.";
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /** Called when the user taps the Start button */
    public void buttonStartGame(View view) {
        // Save user name in SharedPreferences
        EditText nameBox = (EditText) findViewById(R.id.name_box);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("user_name", nameBox.getText().toString());
        editor.apply();

        // If no maps are downloaded, then user cannot play
        // map_exists is set to true after the successful completion of the first download task
        Boolean map_exists = settings.getBoolean("map_exists", false /*Default value */);

        if (map_exists){
            Intent intent = new Intent(this, Activity_2_Choose_Song.class);
            startActivity(intent);
        } else {
            // Tell user that there are no maps to play
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
