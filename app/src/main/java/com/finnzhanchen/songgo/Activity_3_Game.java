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
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

// WRITTEN BY ME: FINN ZHAN CHEN
// ALL THIRD PARTY CODES ARE DOCUMENTED

public class Activity_3_Game extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    // Could use Map to implement Treemap if requires order for logn seach algorithm
    private HashMap<Marker, Placemark> markerMap = new HashMap<Marker, Placemark>();
    // Collected Placemarks
    private ArrayList<Placemark> collected_placemarks = new ArrayList<>();
    private GoogleMap mMap;
    // Add coloured captureCircle with radius circle_radius around current location.
    // All markers within the radius are captured
    Circle captureCircle;
    int capture_circle_radius;
    // Song selected from previous screen
    Song song_selected = null;
    // Lyrics for this song
    ArrayList<ArrayList<String>> lyrics = new ArrayList<ArrayList<String>>();

    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3_game);
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
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Visualise current position with a small blue captureCircle
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            System.out.println("Security exception thrown [onMapReady]");
        }
        // Add "My location" and "Zoom" button to the user interface
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Intent intent = getIntent();
        song_selected = (Song) intent.getSerializableExtra("song_selected");
        String difficulty = intent.getStringExtra("difficulty_selected");

        switch (difficulty){
            case "Novice":
                capture_circle_radius = 50;
                break;
            case "Easy":
                capture_circle_radius = 40;
                break;
            case "Normal":
                capture_circle_radius = 30;
                break;
            case "Hard":
                capture_circle_radius = 20;
                break;
            case "Extreme":
                capture_circle_radius = 15;
                break;
            default:
                capture_circle_radius = 50;
                break;
        }
        // Initialise the captureCircle
        captureCircle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(0, 0))
                .radius(capture_circle_radius)
                .strokeColor(Color.parseColor("#d52133")));
                        /*.fillColor(Color.parseColor("#d52133"))*/

        //Log.e("song", song + difficulty);
        loadPlacemarksOnMap(song_selected.number, difficulty);

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
        // Can we access the user’s current location?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            // Add user name to navigation drawer
            SharedPreferences settings = getSharedPreferences("mysettings",
                    Context.MODE_PRIVATE);
            TextView nameBox = (TextView) findViewById(R.id.user_name_text);
            String name = settings.getString("user_name", "" /*Default*/);
            nameBox.setText(name);

            // When connected, move camera to last known user location
            LatLng myLastLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(myLastLocation, 18);
            mMap.animateCamera(myLocation);

            // Initialise game properties in navigation drawer view
            NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
            Menu menu = nav_view.getMenu();

            // Add selected song to navigation drawer
            MenuItem songBox = menu.findItem(R.id.item_song);
            songBox.setTitle("Song: " + song_selected.number);

            // Add difficulty to navigation drawer
            MenuItem difficultyBox = menu.findItem(R.id.item_difficulty);
            String difficulty = getIntent().getStringExtra("difficulty_selected");
            difficultyBox.setTitle("Difficulty: " + difficulty);


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(Location current) {
        System.out.println("[onLocationChanged] Lat/long now ("
                + String.valueOf(current.getLatitude())
                + ","
                + String.valueOf(current.getLongitude()));

        // Center the captureCircle to the current location
        captureCircle.setCenter(new LatLng(current.getLatitude(), current.getLongitude()));

        for (Marker marker : markerMap.keySet()){
            Location marker_location = new Location("");
            marker_location.setLatitude(marker.getPosition().latitude);
            marker_location.setLongitude(marker.getPosition().longitude);
            // If markers within x metres, collect it and remove from map
            if (current.distanceTo(marker_location) < capture_circle_radius){
                collected_placemarks.add(markerMap.get(marker));
                marker.remove();
            }


        }



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

    // Written by me
    private void loadPlacemarksOnMap(String song, String difficulty){
        String url = null;
        switch (difficulty){
            case "Novice":
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song + "/map5.kml";
                break;
            case "Easy":
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song + "/map4.kml";
                break;
            case "Normal":
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song + "/map3.kml";
                break;
            case "Hard":
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song + "/map2.kml";
                break;
            case "Extreme":
                url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/" + song + "/map1.kml";
                break;
            default:
                break;
        }
        Log.e("URL", url);
        new DownloadPlacemarkTask(mMap, markerMap).execute(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog giveUp = AskOption();
            giveUp.show();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_view_collected_words) {
            Intent intent = new Intent(this, Activity_4_View_Collected_Song.class);
            ArrayList<String> unclassified_placemarks = new ArrayList<>();
            ArrayList<String> veryinteresting_placemarks = new ArrayList<>();
            ArrayList<String> interesting_placemarks = new ArrayList<>();
            ArrayList<String> notboring_placemarks = new ArrayList<>();
            ArrayList<String> boring_placemarks = new ArrayList<>();
            String song_url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"
                    + song_selected.number + "/words.txt";

            new DownloadLyricsTask(lyrics).execute(song_url);

            for (Placemark placemark : collected_placemarks){
                Log.e("Inside FOR loop", "Reached");
                switch (placemark.description){
                    case "unclassified":
                        unclassified_placemarks.add(placemark.position);
                        break;
                    case "veryinteresting":
                        veryinteresting_placemarks.add(placemark.position);
                        break;
                    case "interesting":
                        interesting_placemarks.add(placemark.position);
                        break;
                    case "notboring":
                        notboring_placemarks.add(placemark.position);
                        break;
                    case "boring":
                        boring_placemarks.add(placemark.position);
                        break;
                    default:
                        break;
                }
            }

            intent.putStringArrayListExtra(
                    "unclassified_placemarks", unclassified_placemarks);
            intent.putStringArrayListExtra(
                    "veryinteresting_placemarks", veryinteresting_placemarks);
            intent.putStringArrayListExtra(
                    "interesting_placemarks", interesting_placemarks);
            intent.putStringArrayListExtra(
                    "notboring_placemarks", notboring_placemarks);
            intent.putStringArrayListExtra(
                    "boring_placemarks", boring_placemarks);

            startActivity(intent);

        } else if (id == R.id.item_guess_song) {

        } else if (id == R.id.item_superpower) {

        } else if (id == R.id.item_give_up) {
            AlertDialog giveUp = AskOption();
            giveUp.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Code reuse from https://stackoverflow.com/questions/24359667/how-to-disable-back-button-for-particular-activity
    private AlertDialog AskOption()
    {
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



}
