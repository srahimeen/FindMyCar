package com.hallnguyenrahimeen.findmycar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class HistoryFragment extends Fragment implements OnItemClickListener,
        OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "location_list";

    //final Context context = this;
    Activity activity;
    ListView locationListView;
    ArrayList<StoredLocation> employees;

    ListAdapter listAdapter;
    DBHandler locationHandler;

    private GetLocationTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        locationHandler = new DBHandler(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container,
                false);
        findViewsById(view);

        task = new GetLocationTask(activity);
        task.execute((Void) null);

        locationListView.setOnItemClickListener(this);
        locationListView.setOnItemLongClickListener(this);
        // Employee e = employeeDAO.getEmployee(1);
        // Log.d("employee e", e.toString());
        return view;
    }

    private void findViewsById(View view) {
        locationListView = (ListView) view.findViewById(R.id.garage_list);
    }

    @Override
    public void onResume() {
        //COME BACK TO THIS, IDK WHAT IT DOES BUT IT BROKE THE THING
        //getActivity().setTitle(R.string.app_name);
        //getActivity().getActionBar().setTitle(R.string.app_name);
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> list, View arg1, int position,
                            long arg3) {
        StoredLocation location = (StoredLocation) list.getItemAtPosition(position);

        if (location != null) {
            Log.d("Pressed: ", "Short click");
            //Bundle arguments = new Bundle();
            //arguments.putParcelable("selectedLocation", location);
            //CustomEmpDialogFragment customEmpDialogFragment = new CustomEmpDialogFragment();
            //customEmpDialogFragment.setArguments(arguments);
            //customEmpDialogFragment.show(getFragmentManager(),
            //        CustomEmpDialogFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long arg3) {
        final StoredLocation location = (StoredLocation) parent.getItemAtPosition(position);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Delete location record?.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        locationHandler.deleteLocation(location);
                        listAdapter.remove(location);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        // Use AsyncTask to delete from database

        return true;
    }

    public class GetLocationTask extends AsyncTask<Void, Void, ArrayList<StoredLocation>> {

        private final WeakReference<Activity> activityWeakRef;

        public GetLocationTask(Activity context) {
            this.activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected ArrayList<StoredLocation> doInBackground(Void... arg0) {
            ArrayList<StoredLocation> locationList = locationHandler.getAllLocations();
            return locationList;
        }

        @Override
        protected void onPostExecute(ArrayList<StoredLocation> empList) {
            if (activityWeakRef.get() != null
                    && !activityWeakRef.get().isFinishing()) {
                Log.d("employees", empList.toString());
                employees = empList;
                if (empList != null) {
                    if (empList.size() != 0) {
                        listAdapter = new ListAdapter(activity,
                                empList);
                        locationListView.setAdapter(listAdapter);
                    } else {
                        Toast.makeText(activity, "No Employee Records",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }


    // This method is invoked from MainActivity onFinishDialog() method. It is
    //called from CustomEmpDialogFragment when an employee record is updated.
    //This is used for communicating between fragments.

    public void updateView() {
        task = new GetLocationTask(activity);
        task.execute((Void) null);
    }
}
/* OLD STUFF, keeping just in case stuff doesn't work
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



//A simple {@link Fragment} subclass.


public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}
*/
