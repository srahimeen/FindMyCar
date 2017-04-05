package com.hallnguyenrahimeen.findmycar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant;

import static com.hallnguyenrahimeen.findmycar.activities.MainActivity.currUserData;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.lastLat;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.lastLon;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.markedLat;
import static com.hallnguyenrahimeen.findmycar.fragments.MainFragment.markedLon;


/**
 * Created by tinnn on 3/29/2017.
 */

public class CompassFragmentActivity extends FragmentActivity implements SensorEventListener, com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant.CompassAssistantListener {
    private CompassAssistant CompassAssistant;
    private float currentDegree;

    private SensorManager sensorManager;
    private Sensor compass;
    private ImageView image;
    private TextView compassAngle; // May be used to display the degrees
    private TextView floorLevel;

    public static boolean removePin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        image = (ImageView)findViewById(R.id.imageViewCompass);
        compassAngle = (TextView)findViewById(R.id.angle);
        floorLevel = (TextView) findViewById(R.id.floorNum);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(compass != null){
            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        CompassAssistant = new CompassAssistant(CompassFragmentActivity.this);
        CompassAssistant.addListener(CompassFragmentActivity.this);
        CompassAssistant.start();

        // Displays users floor number on compass navigation
        if (currUserData.getUserFloorNumber() != null) {
            floorLevel.setText("Floor: " + currUserData.getUserFloorNumber());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        this.CompassAssistant.stop();
    }

    @Override
    public void onNewDegreesToNorth(float degrees) {
        // this is not used here because we want to have a smooth moving compass.
    }

    @Override
    public void onNewSmoothedDegreesToNorth(float degrees) {

        final RotateAnimation ra = new RotateAnimation(
                currentDegree,
                degrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setDuration(210);
        ra.setFillAfter(true);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.startAnimation(ra);
            }
        });

       // Continuously finds the degree between user's current location which is last and destination which is marked.
        float bearing = CompassAssistant.getBearingBetweenLocations(lastLat, lastLon, markedLat,markedLon);

        currentDegree = bearing;
        //currentDegree = degrees; For magnetic north
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public void onCompassStopped() {

    }

    @Override
    public void onCompassStarted() {

    }
}