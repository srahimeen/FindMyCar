package com.hallnguyenrahimeen.findmycar;

import com.google.android.gms.maps.model.LatLng;

import java.security.Timestamp;

public class StoredLocation {
    private int id;
    private double lat;
    private double lng;
    private String time;

    //Constructors
    public StoredLocation()
    {
    }
    public StoredLocation(int id, double lat, double lng, String time)
    {
        this.id=id;
        this.lat=lat;
        this.lng=lng;
        this.time=time;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public void setTime(String time) {
        this.time = time;
    }

    //Getters
    public int getId() {
        return id;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
    public String getTime() {
        return time;
    }
}
