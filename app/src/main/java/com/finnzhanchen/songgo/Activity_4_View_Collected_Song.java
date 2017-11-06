package com.finnzhanchen.songgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class Activity_4_View_Collected_Song extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("Activity", "4 Reached");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4_view_collected_song);
        ExpandableListView expandList = findViewById(R.id.expanded_list_view);
        ExpandableListViewAdapter_V2 adapter =
                new ExpandableListViewAdapter_V2(this,
                        getIntent().getStringArrayListExtra("unclassified_words"),
                        getIntent().getStringArrayListExtra("veryinteresting_words"),
                        getIntent().getStringArrayListExtra("interesting_words"),
                        getIntent().getStringArrayListExtra("notboring_words"),
                        getIntent().getStringArrayListExtra("boring_words"));

        expandList.setAdapter(adapter);

    }
}
