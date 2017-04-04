package com.hallnguyenrahimeen.findmycar.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;
import com.hallnguyenrahimeen.findmycar.R;
import com.hallnguyenrahimeen.findmycar.data.DBHandler;
import com.hallnguyenrahimeen.findmycar.data.StoredLocation;

import java.lang.ref.WeakReference;

public class GarageInfoFragment extends Fragment {
    SharedPreferences pref;
    public static final String mypreference = "mypref";
    public static final String pinned = "pinned";

    private class ViewHolder {
        TextView locLatText;
        TextView locLngText;
        TextView locTimeText;
        TextView locLocText;
        TextView garageText;
    }

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
        //Still working on trying to set info if car is parked
        if(pref.contains(pinned)) {
            if(pref.getBoolean(pinned, false)) {
                ViewHolder holder = new ViewHolder();
                location = locationHandler.getMostRecentLocation();
                Log.d("Stuff: ", location.getLoc() + " " + location.getTime());
                if(location != null){
                    holder.garageText = (TextView) activity
                            .findViewById(R.id.parked_details);
                    //holder.garageText.setText(location.getLoc() + " " + location.getTime() + "");
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garage_info, container,
                false);
        findViewsById(view);

        //task = new GetLocationTask(activity);
        //task.execute((Void) null);
        return view;
    }

    private void findViewsById(View view) {
        locationView = view.findViewById(R.id.parked_details);
    }

    @Override
    public void onResume() {
        //COME BACK TO THIS, IDK WHAT IT DOES BUT IT BROKE THE THING
        //getActivity().setTitle(R.string.app_name);
        //getActivity().getActionBar().setTitle(R.string.app_name);
        super.onResume();
    }
    /*
    public class GetLocationTask extends AsyncTask<Void, Void, StoredLocation> {

        private final WeakReference<Activity> activityWeakRef;

        public GetLocationTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected StoredLocation doInBackground(Void... arg0) {
            StoredLocation locationList = locationHandler.getAllLocations();
            return locationList;
        }


        @Override
        protected void onPostExecute(StoredLocation locList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("locations", locList.toString());
                locations = locList;
                if (locList != null) {
                    if (locList.size() != 0) {
                        listAdapter = new ListAdapter(activity,
                                locList);
                        locationListView.setAdapter(listAdapter);
                    } else {
                        Toast.makeText(activity, "No Recorded Locations",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }
    */

    // This method is invoked from MainActivity onFinishDialog() method. It is
    //called from CustomEmpDialogFragment when an employee record is updated.
    //This is used for communicating between fragments.

    public void updateView() {
        //task = new GetLocationTask(activity);
        //task.execute((Void) null);
    }
}
