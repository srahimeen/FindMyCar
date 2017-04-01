package com.hallnguyenrahimeen.findmycar.activities;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant;


/**
 * Created by tinnn on 3/29/2017.
 */

public class CompassActivity extends AppCompatActivity implements SensorEventListener, com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant.CompassAssistantListener {
    private CompassAssistant CompassAssistant;
    private float currentDegree;

    private SensorManager sensorManager;
    private Sensor compass;
    private ImageView image;
    private TextView compassAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        image = (ImageView)findViewById(R.id.imageViewCompass);
        compassAngle = (TextView)findViewById(R.id.angle);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        compass = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(compass != null){
            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // this assistant will point to the magnetic north. If you want to have a compass that points
        // to the geographic north, you have to put a location into the constructor.
        CompassAssistant = new CompassAssistant(CompassActivity.this);
        CompassAssistant.addListener(CompassActivity.this);
        CompassAssistant.start();
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

        currentDegree = degrees;

        /*
        If you want you can get the bearing between two locations right here.
        If you do this in this function you can be sure that the compassassistant is started and
        you will get continously new degrees:

        float bearing = assistant.getBearingBetweenLocations(currLocLat, currLocLng, destLocLat, destLocLng);

        */

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