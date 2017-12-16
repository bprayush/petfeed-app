package com.bugthedebugger.petfeed_test;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class StatusFragment extends Fragment {


    private String local_device_status;
    private String global_device_status;

    @SuppressLint("ValidFragment")
    public StatusFragment(String local_device_status, String global_device_status) {
        // Required empty public constructor
        this.local_device_status = local_device_status;
        this.global_device_status = global_device_status;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_status, container, false);
        view.setBackgroundColor(Color.WHITE);

        TextView status = view.findViewById(R.id.localStatusTv);
        TextView gStatus = view.findViewById(R.id.globalStatusTv);

        if (local_device_status.equals("offline")) {
            status.setTextColor(RED);
            status.setText(local_device_status);
        } else {
            status.setTextColor(GREEN);
            status.setText(local_device_status);
        }

        if (global_device_status.equals("offline")) {
            gStatus.setTextColor(RED);
            gStatus.setText(global_device_status);
        } else {
            gStatus.setTextColor(GREEN);
            gStatus.setText(global_device_status);
        }

        return view;
    }

}
