package com.hallnguyenrahimeen.findmycar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.data.DBHandler;
import com.hallnguyenrahimeen.findmycar.data.StoredLocation;

public class GarageInfoFragment extends Fragment {
    SharedPreferences pref;
    public static final String mypreference = "MYPREF";
    public static final String pinned = "PIN_SAVE";

    public GarageInfoFragment() {
        // Required empty public constructor
    }

    Activity activity;
    View locationView;
    //ListView locationListView;
    //ArrayList<StoredLocation> locations;
    StoredLocation location;

    DBHandler locationHandler;

   // private GetLocationTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        locationHandler = new DBHandler(activity);
        pref = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garage_info, container,
                false);

        if(pref.contains(pinned)) {
            if(pref.getBoolean(pinned, false)) {
                //ViewHolder holder = new ViewHolder();
                location = locationHandler.getMostRecentLocation();
                if(location != null){
                    TextView t = (TextView) view.findViewById(R.id.parked_details);
                    Double lat = location.getLat();
                    Double lng = location.getLng();
                    String parkedText = location.getLoc() + " \n" + location.getTime() + " \nLatitude: " + location.getLat() + " \nLongitude: " + location.getLng();
                    if(lat > 28.0582100 && lat < 28.0592500 && lng > -82.4176300 && lng < -82.4166100){
                        parkedText += "\n\nGarage Info: Richard A. Beard Parking Garage\nPermits: S, R, GZ8, D";
                    }
                    else if(lat > 28.0611300 && lat < 28.0619800 && lng > -82.4127100 && lng < -82.4112400){
                        parkedText += "\n\nGarage Info: Collins Blvd. Parking Facility\nPermits: GZ1, S";
                    }
                    else if(lat > 28.0646800 && lat < 28.0656250 && lng > -82.4124760 && lng < -82.4117400){
                        parkedText += "\n\nGarage Info: Crescent Hill Parking Garage\nPermits: S, E, D";
                    }
                    else if(lat > 28.0663870 && lat < 28.0672650 && lng > -82.4189150 && lng < -82.4175740){
                        parkedText += "\n\nGarage Info: Laurel Drive Parking Garage\nPermits: S, GZ42";
                    }
                    t.setText(parkedText);
                }
            }
        }
        else{
            //Nothin
        }
        findViewsById(view);
        return view;
    }

    private void findViewsById(View view) {
        locationView = view.findViewById(R.id.parked_details);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // This method is invoked from MainActivity onFinishDialog() method. It is
    //called from CustomEmpDialogFragment when an employee record is updated.
    //This is used for communicating between fragments.

    public void updateView() {
        //task = new GetLocationTask(activity);
        //task.execute((Void) null);
    }
}
