package com.hallnguyenrahimeen.findmycar.data;

import com.google.android.gms.maps.model.LatLng;

import java.security.Timestamp;

public class StoredLocation {
    private int id;
    private double lat;
    private double lng;
    private String time;
    private String loc;

    //Constructors
    public StoredLocation()
    {
    }
    public StoredLocation(int id, double lat, double lng, String time, String loc)
    {
        this.id=id;
        this.lat=lat;
        this.lng=lng;
        this.time=time;
        this.loc=loc;
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
    public void setLoc(String loc) {this.loc = loc; }

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
    public String getLoc() {
        return loc;
    }
}
