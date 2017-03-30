package com.hallnguyenrahimeen.findmycar.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant;
import com.hallnguyenrahimeen.findmycar.R;

/**
 * Created by tinnn on 3/29/2017.
 */

public class CompassActivity extends Activity implements com.hallnguyenrahimeen.findmycar.helpers.CompassAssistant.CompassAssistantListener {

    private CompassAssistant CompassAssistant;
    //private float currentDegree;

    private SensorManager sensorManager;
    private ImageView compass;
    private ImageView image;
    private TextView compassAngle;
    private float currentDegree = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.compass = (ImageView) findViewById(R.id.compass_view);
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
                compass.startAnimation(ra);
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
    public void onCompassStopped() {
        // the compass has stopped. Do maybe
    }

    @Override
    public void onCompassStarted() {
        // you can do things here for example if you want to hide a loading indicator.
    }
}