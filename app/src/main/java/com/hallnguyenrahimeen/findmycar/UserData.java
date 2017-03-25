package com.hallnguyenrahimeen.findmycar;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Soufin Rahimeen on 3/25/2017.
 */

public class UserData {

    private LatLng userLatLng;

    public UserData() {
        userLatLng = null;
    }

    public void setUserLatLng(LatLng latlng){
        userLatLng = latlng;
    }

    public LatLng getUserLatLng(){
        return userLatLng;
    }
}
