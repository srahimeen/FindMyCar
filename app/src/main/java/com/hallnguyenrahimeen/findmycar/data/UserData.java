package com.hallnguyenrahimeen.findmycar.data;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.android.gms.maps.model.LatLng;

public class UserData {

    private LatLng userLatLng;
    private Integer userFloorNumber;

    public UserData() {
        userLatLng = null;
    }

    public void setUserLatLng(LatLng latlng){

        userLatLng = latlng;
    }

    public LatLng getUserLatLng(){

        return userLatLng;
    }

    public void setUserFloorNumber(Integer floorNum) {

        userFloorNumber = floorNum;
    }

    public Integer getUserFloorNumber() {

        return userFloorNumber;
    }

}
