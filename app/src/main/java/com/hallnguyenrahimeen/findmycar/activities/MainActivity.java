package com.hallnguyenrahimeen.findmycar.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.hallnguyenrahimeen.findmycar.data.DBHandler;
import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.data.StoredLocation;
import com.hallnguyenrahimeen.findmycar.data.UserData;
import com.hallnguyenrahimeen.findmycar.fragments.GarageInfoFragment;
import com.hallnguyenrahimeen.findmycar.fragments.HistoryFragment;
import com.hallnguyenrahimeen.findmycar.fragments.MainFragment;
import com.hallnguyenrahimeen.findmycar.fragments.RequestFloorDialogFragment;
import com.hallnguyenrahimeen.findmycar.fragments.SettingsFragment;
import com.hallnguyenrahimeen.findmycar.helpers.LocationAddress;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.pinLocation;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.pinLatLng;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener, RequestFloorDialogFragment.Communicator, NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //declared as global so we have access to it
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private GoogleApiClient mGoogleApiClient; // TODO Delete?
    public static UserData currUserData;
    public static UserData[] currUserDataArray = new UserData[50]; //used for multiple markers

    // Stores data to users device using SharedPreferences
    SharedPreferences pref;
    public static final String MYPREF = "MYPREF";
    public static final String PIN_SAVE = "PIN_SAVE";
    public static final String FLOOR_SAVE = "FLOOR_SAVE";

    boolean pinnedCheck = false; // Stores a check if user leaves the app with the map PIN_SAVE

    //sensor variables
    float mPressureValue = 0.0f;
    float mHeight = 0.0f;
    private Integer pressureBasedFloor = 0;

    //check if device has pressure sensor, setup in OnCreate
    boolean hasBarometer = false;


    //permissions value
    public static final int MULTIPLE_PERMISSIONS = 100;

    //pressure sensor
    @Override
    public void onSensorChanged(SensorEvent event) {

        //if you use this listener as listener of only one sensor (ex, Pressure), then you don't need to check sensor type.
        //if a pressure sensor exists, use it to calculate height
        if (hasBarometer) {
            if( Sensor.TYPE_PRESSURE == event.sensor.getType() ) {
                mPressureValue = event.values[0];
                System.out.println("Pressure" + mPressureValue);
                mHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, mPressureValue);
                System.out.println("Height" + mHeight);
                pressureBasedFloor = Math.round(mHeight);
                currUserData.setUserFloorNumber(pressureBasedFloor);
            }
        }
    }

    //pressure sensor
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //when the dialog is made, this method runs from the communicator interface
    @Override
    public void onDialogMessage(Integer floorNum) {
         Integer floorNumber = floorNum; //get the floor number the user picks in the dialog
        currUserData.setUserFloorNumber(floorNumber); //assign the floor number to currUserData
        Log.d("floor number", currUserData.getUserFloorNumber().toString());
    }

    //method to present dialog when FAB is present
    public void showRequestFloorDialog(View v) {
        FragmentManager manager = getSupportFragmentManager();
        RequestFloorDialogFragment requestFloorDialog = new RequestFloorDialogFragment();
        requestFloorDialog.show(manager, "Floor");
    }

    // function to check permissions
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) + ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, MULTIPLE_PERMISSIONS); //request the permission
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, MULTIPLE_PERMISSIONS); //request the permission
                }
            }
        } else {
            // put your function here
        }
    }

    // function to request permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean coarseLocationPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFilePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLoactionPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean internetPermissions = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    if(coarseLocationPermission && writeExternalFilePermission && fineLoactionPermission && internetPermissions)
                    {
                        // put your function here
                    }
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, MULTIPLE_PERMISSIONS);
                    }
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the fragment initially
        final MainFragment fragment = new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Shared Preferences
        pref = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        if(pref.contains(PIN_SAVE)) {
            pinnedCheck = pref.getBoolean(PIN_SAVE, false);
        }


        //handler for the database
        final DBHandler db = new DBHandler(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy, hh:mm a");
        final String format = simpleDateFormat.format(new Date());
        final int i = 100;
        class GeocoderHandler extends Handler {
            @Override
            public void handleMessage(Message message) {
                String locationAddress;
                switch (message.what) {
                    case 1:
                        Bundle bundle = message.getData();
                        locationAddress = bundle.getString("address");
                        break;
                    default:
                        locationAddress = null;
                }

                currUserData.setUserLatLng(pinLatLng); //add PIN_SAVE latlang to UserData
                pinnedCheck = true;
                db.addLocation(new StoredLocation(i, pinLatLng.latitude, pinLatLng.longitude, format, locationAddress));
                        //tvAddress.setText(locationAddress);
            }
        }

        //fab stuff
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Allows preferences to updated
                SharedPreferences.Editor editor = pref.edit();

                // Parking Mode --> Pins the current location and changes to Navigation mode
                if (pinLocation != null && !pinnedCheck) { // prevents crash if GPS is left off
                    currUserData.setUserLatLng(pinLatLng); //add PIN_SAVE latlang to UserData
                    // Changes the FAB to compass mode
                    fab.setImageResource(R.drawable.ic_fabreturn);

                    // Pin the location on the map
                    fragment.drawMarker(currUserData.getUserLatLng());

                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(pinLatLng.latitude, pinLatLng.longitude,
                            getApplicationContext(), new GeocoderHandler());
                    //db.addLocation(new StoredLocation(i,pinLatLng.latitude,pinLatLng.longitude, format));

                    //Printing location info
                    /*
                    Log.d("Reading: ", "Reading all locations..");
                    List<StoredLocation> locations = db.getAllLocations();

                    for (StoredLocation location : locations) {
                        String log = "Id: " + location.getId() + " ,Lat: " + location.getLat() + " ,Lng: "
                                + location.getLng() + " ,Time: " + location.getTime() + " ,Loc: " + location.getLoc();
                        // Writing locations to log
                        Log.d("Location: : ", log);
                    }
                    */
                    //Storing data as KEY/VALUE pair in the device
                    editor.putBoolean(PIN_SAVE, true);
                    pinnedCheck = true;
                    

                    //if user has no barometer sensor, request dialog
                    if (!hasBarometer) {
                        showRequestFloorDialog(view);
                    }


                    editor.commit(); // Save the changes in SharedPreferences + commit changes


                }
                // Navigation Mode --> Displays compass pointing to PIN_SAVE location and reverts to Parking upon exit
                else if (pinnedCheck) {
                    Toast.makeText(MainActivity.this, R.string.compassMessage, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, CompassFragmentActivity.class);
                    startActivity(intent);

                    // Reverts back to parking mode once exited
                    pinnedCheck = false;
                    editor.remove(PIN_SAVE);
                    editor.commit(); // commit changes

                    // Sets FAB back to parking mode
                    fab.setImageResource(R.drawable.ic_fab);

                    // Clears map marker from map
                    fragment.clearMap();
                    currUserData.setUserLatLng(null);
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.gpsOff, Toast.LENGTH_SHORT).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //check permissions here
        checkPermission();

        //create user data instance
        currUserData = new UserData();

        //check if barometer sensor exists
        hasBarometer = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER);

        // Printing all locations
        /*
        Log.d("Reading: ", "Reading all shops..");
        List<StoredLocation> locations = db.getAllLocations();

        for (StoredLocation location : locations) {
            String log = "Id: " + location.getId() + " ,Lat: " + location.getLat() + " ,Lng: "
                    + location.getLng() + " ,Time: " + location.getTime();
            // Writing locations to log
            Log.d("Location: : ", log);
        }
        */

        // Checks if a pin has already been placed previously and restores navigation view
        if (pinnedCheck) {
            fab.setImageResource(R.drawable.ic_fabreturn);
            Log.d("Pinned WAS CHECKED: ", "YES");
            StoredLocation location = db.getMostRecentLocation();
            String log = "Id: " + location.getId() + " ,Lat: " + location.getLat() + " ,Lng: "
                    + location.getLng() + " ,Time: " + location.getTime() + " ,Loc: " + location.getLoc();
            // Writing locations to log
            Log.d("Location: ", log);

            // Restore Navigation
            pinLatLng = new LatLng(location.getLat(), location.getLng());
            currUserData.setUserLatLng(pinLatLng); //add PIN_SAVE latlang to UserData
            // Restore Floor Number
            //currUserData.setUserFloorNumber(pref.getInt(FLOOR_SAVE, 0));
        }
        else {
            fab.setImageResource(R.drawable.ic_fab);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exitTitle)
                    .setMessage(R.string.exitMessage)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_information) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            // Redirect to main fragment
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_history) {
            // Redirect to history fragment
            HistoryFragment fragment = new HistoryFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_garage_info) {
            // Redirect to garage info fragment
            GarageInfoFragment fragment = new GarageInfoFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_settings) {
            // Redirect to garage info fragment
            SettingsFragment fragment = new SettingsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }



}
