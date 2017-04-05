package com.hallnguyenrahimeen.findmycar.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.hallnguyenrahimeen.findmycar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFloorDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnOk, btnCancel;
    EditText txtFloorNumberInput;
    public static int floorNumber = 0;

    public RequestFloorDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_floor_dialog, container, false);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        txtFloorNumberInput  = (EditText) view.findViewById(R.id.input_floor);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_ok){
            String input  = txtFloorNumberInput.getText().toString();
            try {
                floorNumber = Integer.parseInt(input);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid floor number", Toast.LENGTH_LONG).show();
            }
        }
        else {

        }
    }

}
