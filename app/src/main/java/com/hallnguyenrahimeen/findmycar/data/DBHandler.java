package com.hallnguyenrahimeen.findmycar.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class DBHandler extends SQLiteOpenHelper {
    // Database Version, change this number to one greater if you update the table schema at all
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "carsInfo";
    // Contacts table name
    private static final String TABLE_STORED_LOCATION = "storedLocation";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOC = "location";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STORED_LOCATION + "("
        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LAT + " REAL,"
        + KEY_LNG + " REAL," + KEY_TIME + " TEXT," + KEY_LOC + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORED_LOCATION);
        // Creating tables again
        onCreate(db);
    }
    // Adding new shop
    public void addLocation(StoredLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("AddLat:::", Double.toString(location.getLat()));
        Log.d("AddLng:::", Double.toString(location.getLng()));
        ContentValues values = new ContentValues();
        values.put(KEY_LAT, location.getLat()); // location lat
        values.put(KEY_LNG, location.getLng()); // location lng
        values.put(KEY_TIME, location.getTime()); // location time
        values.put(KEY_LOC, location.getLoc()); // location info
        Log.d("AddLatAfter:::", Double.toString(values.getAsDouble(KEY_LAT)));
        Log.d("AddLngAfter:::", Double.toString(values.getAsDouble(KEY_LNG)));
        // Inserting Row
        db.insert(TABLE_STORED_LOCATION, null, values);
        db.close(); // Closing database connection
    }
    // Getting one location
    public StoredLocation getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STORED_LOCATION, new String[]{KEY_ID,
                KEY_LAT, KEY_LNG, KEY_TIME, KEY_LOC}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        //id, lat, lng, time, location
        StoredLocation contact = new StoredLocation(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),
                cursor.getString(3), cursor.getString(4));
        // return location
        return contact;
    }

    public StoredLocation getMostRecentLocation(){
        // SELECT max(id), lat, lng, time, location FROM storedLocation GROUP BY lat, lng, time, loc
        // Doesn't care if the car is parked or not, so you need to check that otherwise, this will
        // just return the most recent stored location (which will always have the highest ID)
        /*
        String selectQuery = "SELECT max("+ KEY_ID +"), " + KEY_LAT +", " + KEY_LNG + ", " + KEY_TIME
                + ", " + KEY_LOC + " FROM " + TABLE_STORED_LOCATION + " GROUP BY " + KEY_LAT + ", "
                + KEY_LNG + ", " + KEY_TIME + ", " + KEY_LOC;
        */
        String selectQuery = "SELECT * FROM " + TABLE_STORED_LOCATION + " WHERE " + KEY_ID +
                "=(SELECT max(" + KEY_ID + ") FROM " + TABLE_STORED_LOCATION + ")";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Create new location object to return, this is just a single line way of the default
        // constructor
        Log.d("LAT:::",cursor.getString(1));
        Log.d("LNG:::",cursor.getString(2));
        StoredLocation location = new StoredLocation(Integer.parseInt(cursor.getString(0)),
                Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)),
                cursor.getString(3), cursor.getString(4));
        return location;
    }
    // Getting All locations
    public ArrayList<StoredLocation> getAllLocations() {
        ArrayList<StoredLocation> locationList = new ArrayList<StoredLocation>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STORED_LOCATION + " ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Making new storedLocation object and setting the values
                StoredLocation location = new StoredLocation();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setLat(Double.parseDouble(cursor.getString(1)));
                location.setLng(Double.parseDouble(cursor.getString(2)));
                location.setTime(cursor.getString(3));
                location.setLoc(cursor.getString(4));
                // Adding contact to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }

    // Getting All locations except for the first, used to show the history while the car as parked,
    // as the current parked location isn't history yet technically
    public ArrayList<StoredLocation> getAllButFirstLocations() {
        ArrayList<StoredLocation> locationList = new ArrayList<StoredLocation>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_STORED_LOCATION + " WHERE " + KEY_ID + " <> " +
                "(SELECT max("+ KEY_ID +") FROM " + TABLE_STORED_LOCATION + ") ORDER BY " + KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //Making new storedLocation object and setting the values
                StoredLocation location = new StoredLocation();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setLat(Double.parseDouble(cursor.getString(1)));
                location.setLng(Double.parseDouble(cursor.getString(2)));
                location.setTime(cursor.getString(3));
                location.setLoc(cursor.getString(4));
                // Adding contact to list
                locationList.add(location);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }
    // Getting location count
    public int getLocationCount() {
        String countQuery = "SELECT * FROM " + TABLE_STORED_LOCATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    // Updating a location
    public int updateLocation(StoredLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, location.getLat());
        values.put(KEY_LNG, location.getLng());
        values.put(KEY_TIME, location.getTime());
        values.put(KEY_LOC, location.getLoc());

        // updating row
        return db.update(TABLE_STORED_LOCATION, values, KEY_ID + " = ?",
        new String[]{String.valueOf(location.getId())});
    }

    // Deleting a location
    public void deleteLocation(StoredLocation location) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORED_LOCATION, KEY_ID + " = ?",
        new String[] { String.valueOf(location.getId()) });
        db.close();
    }
}