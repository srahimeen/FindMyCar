package com.hallnguyenrahimeen.findmycar.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.hallnguyenrahimeen.findmycar.data.DBHandler;
import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.data.StoredLocation;
import com.hallnguyenrahimeen.findmycar.data.UserData;
import com.hallnguyenrahimeen.findmycar.fragments.GarageInfoFragment;
import com.hallnguyenrahimeen.findmycar.fragments.HistoryFragment;
import com.hallnguyenrahimeen.findmycar.fragments.MainFragment;
import com.hallnguyenrahimeen.findmycar.fragments.SettingsFragment;
import com.hallnguyenrahimeen.findmycar.helpers.LocationAddress;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.lastLocation;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.pinnedLatLng;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    //declared as global so we have access to it
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    private GoogleApiClient mGoogleApiClient;
    public static UserData currUserData;
    public static UserData[] currUserDataArray = new UserData[50]; //Used for multiple markers
    private boolean mPinned = false; // Checks if pin is placed


    public static final int MULTIPLE_PERMISSIONS = 100;

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

        //handler for the database
        final DBHandler db = new DBHandler(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
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
                fragment.pinLocation(lastLocation);
                currUserData.setUserLatLng(pinnedLatLng); //add pinned latlang to UserData
                mPinned = true;
                db.addLocation(new StoredLocation(i,pinnedLatLng.latitude,pinnedLatLng.longitude, format, locationAddress));
                        //tvAddress.setText(locationAddress);
            }
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lastLocation != null && !mPinned) { // prevents crash if GPS is left off
                    //fragment.pinLocation(lastLocation);
                    //currUserData.setUserLatLng(pinnedLatLng); //add pinned latlang to UserData
                    //mPinned = true;
                    fab.setImageResource(R.drawable.ic_fabreturn);

                    // Stores pinned location into the database
                    //int i = 100;
                    //i=i+1;

                    //Log.d("MainActivity", "Current Timestamp: " + format);
                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(pinnedLatLng.latitude, pinnedLatLng.longitude,
                            getApplicationContext(), new GeocoderHandler());
                    //db.addLocation(new StoredLocation(i,pinnedLatLng.latitude,pinnedLatLng.longitude, format));

                    //Printing location info

                    Log.d("Reading: ", "Reading all locations..");
                    List<StoredLocation> locations = db.getAllLocations();

                    for (StoredLocation location : locations) {
                        String log = "Id: " + location.getId() + " ,Lat: " + location.getLat() + " ,Lng: "
                                + location.getLng() + " ,Time: " + location.getTime() + " ,Loc: " + location.getLoc();
                        // Writing locations to log
                        Log.d("Location: : ", log);
                    }

                } else if (mPinned) {
                    Toast.makeText(MainActivity.this,"A location has already been pinned.", Toast.LENGTH_SHORT).show();
                    //TODO: Make function for compass button, I have been trying this for awhile
                }
                else {
                    Toast.makeText(MainActivity.this,"The GPS is not turned on.", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
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


/*
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.location.Location;
    import android.location.LocationManager;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Message;
    import android.provider.Settings;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;

    public class MyActivity extends Activity {

        Button btnGPSShowLocation;
        Button btnShowAddress;
        TextView tvAddress;

        AppLocationService appLocationService;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my);
            tvAddress = (TextView) findViewById(R.id.tvAddress);
            appLocationService = new AppLocationService(
                    MyActivity.this);

            btnGPSShowLocation = (Button) findViewById(R.id.btnGPSShowLocation);
            btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Location gpsLocation = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);
                    if (gpsLocation != null) {
                        double latitude = gpsLocation.getLatitude();
                        double longitude = gpsLocation.getLongitude();
                        String result = "Latitude: " + gpsLocation.getLatitude() +
                                " Longitude: " + gpsLocation.getLongitude();
                        tvAddress.setText(result);
                    } else {
                        showSettingsAlert();
                    }
                }
            });

            btnShowAddress = (Button) findViewById(R.id.btnShowAddress);
            btnShowAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    Location location = appLocationService
                            .getLocation(LocationManager.GPS_PROVIDER);

                    //you can hard-code the lat & long if you have issues with getting it
                    //remove the below if-condition and use the following couple of lines
                    //double latitude = 37.422005;
                    //double longitude = -122.084095

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LocationAddress locationAddress = new LocationAddress();
                        locationAddress.getAddressFromLocation(latitude, longitude,
                                getApplicationContext(), new GeocoderHandler());
                    } else {
                        showSettingsAlert();
                    }

                }
            });

        }

        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    MyActivity.this);
            alertDialog.setTitle("SETTINGS");
            alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            MyActivity.this.startActivity(intent);
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
*/


}
