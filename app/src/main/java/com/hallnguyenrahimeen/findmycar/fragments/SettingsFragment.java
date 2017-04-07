package com.hallnguyenrahimeen.findmycar.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.hallnguyenrahimeen.findmycar.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static boolean DARK_THEME;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        final CheckBox darkTheme = (CheckBox)rootView.findViewById(R.id.darkTheme);

        darkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DARK_THEME = darkTheme.isChecked();
            }
        });

        return rootView;
    }

}
