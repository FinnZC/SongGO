package com.finnzhanchen.songgo;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

// WRITTEN BY ME: FINN ZHAN CHEN

public class Activity_3_Game extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    // A map of markers pointing to its corresponding placemark,
    // It allows concurrent modification from several threads without the need to block them.
    private ConcurrentHashMap<Marker, Placemark> markerMap = new ConcurrentHashMap<Marker, Placemark>();
    // A copy of the original markerMap for the superpower feature
    private ConcurrentHashMap<Marker, Placemark> markerMapOld;
    // Lyrics for this song
    private HashMap<String, String[]> lyrics = new HashMap<String, String[]>();
    // Collected Placemarks
    private ArrayList<Placemark> collectedPlacemarks = new ArrayList<>();
    // Add coloured captureCircle with radius circle_radius around current location.
    private Circle captureCircle;
    // All markers within the radius are captured in metres
    private int captureCircleRadius;
    // Song selected from previous screen
    private Song songSelected;
    // Default number of guess remaining on a new game
    private int guessRemaining = 5;
    // Default number of superpowerRemaining on a new game
    private int superpowerRemaining = 3;
    // Is superpower active? This prevents superpower activated when it is already active
    private boolean isSuperpowerActive = false;
    // Used to adjust placemarks for bonus feature of playing a map anywhere centered to user
    private LatLng gameStartPosition;
    // Mute or unmute the game sound
    private boolean isMute = true;
    // Is game initialised
    private boolean isGameInitialised = false;
    // Update guess remaining request code
    static final int UPDATE_GUESS_REMAINING_REQUEST = 1;  // The request code
    // settings is initialised in OnCreate
    private SharedPreferences settings;
    // Maps
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_game);
        // Code template provided by the Navigation Drawer Activity
        // https://developer.android.com/training/implementing-navigation/nav-drawer.html
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.game_map);
        mapFragment.getMapAsync(this);
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        //seeInternalStorageFilesList();
        settings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    protected void createLocationRequest() {
        // Set the parameters for the location request
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); // preferably every 5 seconds
        mLocationRequest.setFastestInterval(1000); // at most every second
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Can we access the user’s current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try { createLocationRequest(); }
        catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }

        // Only able to get user's location on onConnected so will initialise the game here
        // Set up the game which require the user's current position1 in setGameStartPosition
        if (!isGameInitialised){
            initialiseGame();
            isGameInitialised = true;
        } else{
            moveCameraToLastKnownPosition();
        }
    }

    @Override
    public void onLocationChanged(Location current) {
        System.out.println("[onLocationChanged] Lat/long now ("
                + String.valueOf(current.getLatitude())
                + ","
                + String.valueOf(current.getLongitude()));
        capturePlacemarksWithinRange(current);
    }

    @Override
    public void onConnectionSuspended(int flag) {
        System.out.println(" >>>> onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        System.out.println(" >>>> onConnectionFailed");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Close drawer is it's open, and if it's closed, ask if user wants to quit game
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog giveUp = AskOption();
            giveUp.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == UPDATE_GUESS_REMAINING_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateGuessRemaining(data.getIntExtra("guessRemaining", 0));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_view_collected_words) {
            viewCollectedWords();

        } else if (id == R.id.item_guess_song) {
            guessSong();

        } else if (id == R.id.item_superpower) {
            activateSuperpower();

        } else if (id == R.id.item_mute){
            toggleMute();

        } else if (id == R.id.item_give_up) {
            AlertDialog giveUp = AskOption();
            giveUp.show();
        }
        return true;
    }

    // Non Override methods

    private void initialiseGame(){
        Intent intent = getIntent();
        songSelected = (Song) intent.getSerializableExtra("songSelected");
        String difficulty = intent.getStringExtra("difficultySelected");
        initialiseGuessRemaining();
        initialiseSuperpower();
        setUI();
        setCaptureRange(difficulty);
        loadSongLyrics();
        setGameStartPosition();
        loadPlacemarksOnMap(difficulty);
        // Move camera to gameStartPosition
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(gameStartPosition, 18);
        mMap.animateCamera(myLocation);

        // Add floor map to the Google Map Not For SongGo
        LatLngBounds atLvl5Bounds = new LatLngBounds(
                new LatLng(55.94426201125635, -3.1871650367975235),       // South west corner
                new LatLng(55.944596588047396, -3.186331540346145));      // North east corner
        GroundOverlayOptions atLvl5Map = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.at_floor5_v4))
                .positionFromBounds(atLvl5Bounds);
        mMap.addGroundOverlay(atLvl5Map);

    }

    private void initialiseSuperpower(){
        // Adds accumulated superpowerRemaining from past victories
        // on top of default number of superpowerRemaining on a new game
        int accumulatedSuperpower = settings.getInt("superpower_remaining", 0 /*Default value on new install*/);
        makeToast("Accumulated " + accumulatedSuperpower + " superpower from previous gameplay!");
        superpowerRemaining = superpowerRemaining + accumulatedSuperpower;

    }

    private void initialiseGuessRemaining(){
        // Adds accumulated guess remaining from past victories
        // on top of default number of guess remaining on a new game
        int accumulatedGuess = settings.getInt("guess_remaining", 0 /*Default value on new install*/);
        makeToast("Accumulated " + accumulatedGuess + " superpower from previous gameplay!");
        guessRemaining = guessRemaining + accumulatedGuess;
    }

    private void setUI(){
        try {
            // Visualise current position with a small blue captureCircle
            mMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {
            System.out.println("Security exception thrown [onMapReady]");
        }

        // Add "My location" and "Zoom" button to the user interface
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Add user name to navigation drawer
        TextView nameBox = (TextView) findViewById(R.id.user_name_text);
        String name = settings.getString("user_name", "" /*Default*/);
        nameBox.setText(name);

        // Initialise game properties in Navigation Drawer view
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        // Add selected song to navigation drawer
        MenuItem songBox = menu.findItem(R.id.item_song);
        songBox.setTitle("Song: " + songSelected.number);
        // Add where the game is played
        MenuItem whereBox = menu.findItem(R.id.item_where);
        whereBox.setTitle("Where: " + getIntent().getStringExtra("whereSelected"));
        // Add difficulty to navigation drawer
        MenuItem difficultyBox = menu.findItem(R.id.item_difficulty);
        String difficulty = getIntent().getStringExtra("difficultySelected");
        difficultyBox.setTitle("Difficulty: " + difficulty);
        // Add guess remaining to navigation drawer
        MenuItem guessRemainingBox = menu.findItem(R.id.item_guess_remaining);
        guessRemainingBox.setTitle("Guess Remaining: " + guessRemaining);
        // Add supowerpower remaining to navigation drawer
        MenuItem superpowerBox = menu.findItem(R.id.item_superpower_remaining);
        superpowerBox.setTitle("Superpower Remaining: " + superpowerRemaining);
        Log.e("SUPERPOWER, GUESS: ", superpowerRemaining + " " + guessRemaining);
    }

    private boolean setGameStartPosition(){
        // Initialise Game Placemarks
        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                // Gets the game starting location
                switch (getIntent().getStringExtra("whereSelected")){
                    case "My Current Location":
                        gameStartPosition =
                                new LatLng(mLastLocation.getLatitude(),
                                        mLastLocation.getLongitude());
                        break;
                    case "George Square, Edinburgh":
                        // Center of the maps built around central campus
                        // Assuming all placemarks are within the latitude and longitude
                        // specified in the coursework
                        gameStartPosition =
                                new LatLng((55.946233 + 55.942617)/2.0,
                                        -(3.192473 + 3.184319)/2.0);
                        break;
                    case  "Wall Street, New York":
                        gameStartPosition = new LatLng(40.706380, -74.009504);
                        break;
                    case "Imperial College, London":
                        gameStartPosition = new LatLng(51.498728, -0.174987);
                        break;
                    case "Las Palmas, Gran Canaria":
                        gameStartPosition = new LatLng(28.099870, -15.454743);
                        break;
                    case "Hollywood, Los Angeles":
                        gameStartPosition = new LatLng(34.089920, -118.321221);
                        break;
                    default:
                        // None of the available options
                        // Will probably never enter this option
                        gameStartPosition = new LatLng(0,0);
                        break;
                }
                return true;
            }
            Log.e("mLastLocation", "is null!");
            return false;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
    }

    private void setCaptureRange(String difficulty){
        switch (difficulty){
            case "Novice":
                captureCircleRadius = 50;
                break;
            case "Easy":
                captureCircleRadius = 40;
                break;
            case "Normal":
                captureCircleRadius = 30;
                break;
            case "Hard":
                captureCircleRadius = 20;
                break;
            case "Extreme":
                captureCircleRadius = 15;
                break;
            default:
                captureCircleRadius = 50;
                break;
        }
        // Initialise the captureCircle
        captureCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(0, 0))
                .radius(captureCircleRadius)
                .strokeColor(Color.parseColor("#d52133")));
    }

    private void loadSongLyrics(){
        String fileName = "Lyrics" + songSelected.number;
        Log.e("Loading:", fileName);
        new LoadLyricsFromFileTask(this, lyrics).execute(fileName);

    }

    private void loadPlacemarksOnMap(String difficulty){
        String fileName = null;
        switch (difficulty){
            case "Novice":
                fileName = "Song" + songSelected.number + "-Map5";
                break;
            case "Easy":
                fileName = "Song" + songSelected.number + "-Map4";
                break;
            case "Normal":
                fileName = "Song" + songSelected.number + "-Map3";
                break;
            case "Hard":
                fileName = "Song" + songSelected.number + "-Map2";
                break;
            case "Extreme":
                fileName = "Song" + songSelected.number + "-Map1";
                break;
            default:
                Log.e("ERROR:", "Difficulty selected not an option");
                break;
        }
        Log.e("FileName:", fileName);
        if (fileName!=null) {
            //getStartGamePosition(fileName);
            new LoadPlacemarksFromFileTask(this, mMap, markerMap, gameStartPosition).execute(fileName);
        }
    }

    private void capturePlacemarksWithinRange(Location current) {
        // Center the captureCircle to the current location
        captureCircle.setCenter(new LatLng(current.getLatitude(), current.getLongitude()));
        // Play sound once only in each location change
        boolean soundPlayed = false;
        // ConcurrentHashMap avoids ConcurrentModificationException
        for (Marker marker : markerMap.keySet()) {
            Location marker_location = new Location("");
            marker_location.setLatitude(marker.getPosition().latitude);
            marker_location.setLongitude(marker.getPosition().longitude);
            // If markers within x metres, collect it and remove from map
            if (current.distanceTo(marker_location) < captureCircleRadius) {
                if (!soundPlayed && !isMute) {
                    new PlaySoundWhenCollectedTask(this).execute();
                    soundPlayed = true;
                }
                collectedPlacemarks.add(markerMap.get(marker));
                marker.remove();
                markerMap.remove(marker);

            }
        }
    }

    private void viewCollectedWords(){
        Intent intent = new Intent(this, Activity_4_View_Collected_Song.class);

        // LinkedHashSet is used to preserve the order in which words are collected
        // and avoids repetition
        LinkedHashSet<String> unclassified_words = new LinkedHashSet<String>();
        LinkedHashSet<String> veryinteresting_words = new LinkedHashSet<String>();
        LinkedHashSet<String> interesting_words = new LinkedHashSet<String>();
        LinkedHashSet<String> notboring_words = new LinkedHashSet<String>();
        LinkedHashSet<String> boring_words = new LinkedHashSet<String>();
        String[] position; // splits "4:23" into line_number and word_index
        String line_number;
        int word_index;
        String word;

        if (collectedPlacemarks.size() > 0) {
            for (Placemark placemark : collectedPlacemarks) {
                position = placemark.position.split(":");
                line_number = position[0];
                word_index = Integer.parseInt(position[1]) - 1;
                // -1 because format provided starts with 1
                word = lyrics.get(line_number)[word_index];
                switch (placemark.description) {
                    case "unclassified":
                        unclassified_words.add(word);
                        break;
                    case "veryinteresting":
                        veryinteresting_words.add(word);
                        break;
                    case "interesting":
                        interesting_words.add(word);
                        break;
                    case "notboring":
                        notboring_words.add(word);
                        break;
                    case "boring":
                        boring_words.add(word);
                        break;
                    default:
                        // Word not in any category is skipped
                        break;
                }
            }
        }

        // Type of collected words are initialised as an ArrayList passed to the intents
        intent.putStringArrayListExtra(
                "unclassified_words",new ArrayList<String>(unclassified_words));
        intent.putStringArrayListExtra(
                "veryinteresting_words", new ArrayList<String>(veryinteresting_words));
        intent.putStringArrayListExtra(
                "interesting_words", new ArrayList<String>(interesting_words));
        intent.putStringArrayListExtra(
                "notboring_words", new ArrayList<String>(notboring_words));
        intent.putStringArrayListExtra(
                "boring_words", new ArrayList<String>(boring_words));

        startActivity(intent);
    }

    private void guessSong(){
        Intent intent = new Intent(this, Activity_5_Guess_Song.class);
        String difficulty = getIntent().getStringExtra("difficultySelected");
        intent.putExtra("difficulty", difficulty);
        intent.putExtra("songSelected", songSelected);
        intent.putExtra("guessRemaining", guessRemaining);
        intent.putExtra("superpowerRemaining", superpowerRemaining);
        intent.putExtra("where", getIntent().getStringExtra("whereSelected"));
        startActivityForResult(intent, UPDATE_GUESS_REMAINING_REQUEST);
    }

    private void activateSuperpower(){
        if (!isSuperpowerActive) {
            superpowerRemaining = superpowerRemaining - 1;
            isSuperpowerActive = true;
            // Telling user that the superpower is active
            makeToast("Superpower is active for 60 seconds!");
            if (superpowerRemaining > 0) {
                String difficulty = getIntent().getStringExtra("difficultySelected");
                String newDifficulty = null;
                switch (difficulty) {
                    case "Novice":
                        // Easiest mode already so no effect
                        newDifficulty = "Novice";
                        break;
                    case "Easy":
                        newDifficulty = "Novice";
                        break;
                    case "Normal":
                        newDifficulty = "Easy";
                        break;
                    case "Hard":
                        newDifficulty = "Normal";
                        break;
                    case "Extreme":
                        newDifficulty = "Hard";
                        break;
                    default:
                        break;
                }

                if (newDifficulty != null) {
                    markerMapOld = new ConcurrentHashMap<Marker, Placemark>(markerMap);
                    mMap.clear(); // Remove all markers on map and capture circle
                    // Initialise new capture range
                    setCaptureRange(newDifficulty);
                    loadPlacemarksOnMap(newDifficulty);
                    activateCountDownTimer(60000, difficulty);
                }

            } else {
                // Telling user that there are no remaining superpower
                makeToast("You have no more superpower!");
            }
        } else {
            // Telling user that the superpower is already active
            makeToast("Superpower is already active!");
        }
    }

    private void activateCountDownTimer(int timeInMilisecond, final String difficulty){
        new CountDownTimer(timeInMilisecond, 1000) {
            public void onTick(long millisUntilFinished) {
                NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
                Menu menu = nav_view.getMenu();
                MenuItem superpowerBox = menu.findItem(R.id.item_superpower_remaining);
                superpowerBox.setTitle("Superpower Time Remaining: " + millisUntilFinished / 1000 + "s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                // Superpower finishes,
                isSuperpowerActive = false;
                // Inform user
                makeToast("Superpower deactivated!");
                // Replace timer with number of superpower remaining
                NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
                Menu menu = nav_view.getMenu();
                MenuItem superpowerBox = menu.findItem(R.id.item_superpower_remaining);
                superpowerBox.setTitle("Superpower Remaining: " + superpowerRemaining);
                // Remove all markers and capture circle
                mMap.clear();
                // Revert to old capture range
                setCaptureRange(difficulty);
                // Remove all markers on map and revert map back with old placemarks
                markerMap = new ConcurrentHashMap<Marker, Placemark>(markerMapOld);
                new ReloadPlacemarksToMapTask(mMap, markerMap).execute();
            }
        }.start();
    }

    private void toggleMute(){
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem muteItem = menu.findItem(R.id.item_mute);
        if (isMute){
            isMute = false;
            muteItem.setTitle("Sound On");
        } else {
            isMute = true;
            muteItem.setTitle("Sound Off");
        }
    }

    private void makeToast(String text){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    private void updateGuessRemaining(int newGuessRemaining){
        guessRemaining = newGuessRemaining;
        // Update guess remaining in navigation drawer
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem guessRemainingBox = menu.findItem(R.id.item_guess_remaining);
        guessRemainingBox.setTitle("Guess Remaining: " + guessRemaining);
    }

    private void moveCameraToLastKnownPosition(){
        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            // When connected, move camera to last known user location
            if (mLastLocation != null) {
                LatLng myLastLocation =
                        new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(myLastLocation, 18);
                mMap.animateCamera(myLocation);
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    // Code reuse from https://stackoverflow.com/questions/24359667/how-to-disable-back-button-for-particular-activity
    private AlertDialog AskOption(){
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to give up?")
                //.setIcon(R.drawable.delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }

    // For testing only, prints out all files in the internal storage
    private void seeInternalStorageFilesList() {
        for (String name :this.fileList()){
            Log.e("File", name);
        }
    }
}
