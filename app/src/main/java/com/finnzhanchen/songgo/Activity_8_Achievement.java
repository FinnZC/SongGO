package com.finnzhanchen.songgo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Activity_8_Achievement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_8_achievement);
    }

    public void onButtonWatch(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/fJ9rUzIMcZQ")));
        Log.i("Video", "Video Playing....");

    }
 }
