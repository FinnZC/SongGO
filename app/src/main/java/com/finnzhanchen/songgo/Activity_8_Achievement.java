package com.finnzhanchen.songgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class Activity_8_Achievement extends AppCompatActivity {
    private ArrayList<String> completedSongsList = new ArrayList<>();
    private HashMap<String, String> songToURLMap = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_achievement);
        SharedPreferences settings = getSharedPreferences("mysettings",
                Context.MODE_PRIVATE);
        // Convert the saved completed songs set into an ArrayList for updating ListView in UI
        Set<String> achievements =
                settings.getStringSet("completed_songs", null);

        // Checks if achievements is empty
        if (achievements != null) {
            for (String achievementDetail : achievements) {
                // special symbol to seperate achievement detail and link
                String[] achievementDetailSplit = achievementDetail.split(",");
                completedSongsList.add(achievementDetailSplit[0]);
                songToURLMap.put(achievementDetailSplit[0], achievementDetailSplit[1]);
                Log.e("Details: ", achievementDetailSplit[0]);
                Log.e("URL: ", achievementDetailSplit[1]);

            }

            // Sort the ArrayList alphabetically (by time) so latest completed song is last
            Collections.sort(completedSongsList); // O(n*log(n)) sorting algorithm

            final ListView listView = (ListView) findViewById(R.id.listview);
            ArrayAdapter<String> listAdapter =
                    new ArrayAdapter<String>(this, R.layout.simplerow, completedSongsList);
            listView.setAdapter(listAdapter);

            // Redirect to Youtube when clicked on a song
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String songURL = songToURLMap.get(completedSongsList.get(position));
                    Log.i("Video", "Video Playing....");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(songURL)));
                }
            });
        }
    }
 }
