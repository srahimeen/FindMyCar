package com.hallnguyenrahimeen.findmycar.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hallnguyenrahimeen.findmycar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFloorDialogFragment extends DialogFragment implements View.OnClickListener {

    Button btnOk, btnCancel;
    EditText txtFloorNumberInput;
    private Integer mFloorNumber = 0;
    Communicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (Communicator) context;
    }

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
                mFloorNumber = Integer.parseInt(input);
                communicator.onDialogMessage(mFloorNumber);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Invalid floor number", Toast.LENGTH_LONG).show();
            }

            dismiss();
        }
        else {
            mFloorNumber = 0;
            dismiss();
        }
    }

    //interface to send data from dialog to main activity
    public interface Communicator
    {
        public void onDialogMessage(Integer floorNum);
    }
}
