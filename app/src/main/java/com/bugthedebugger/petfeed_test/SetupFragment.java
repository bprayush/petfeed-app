package com.bugthedebugger.petfeed_test;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetupFragment extends Fragment {


    public SetupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setup, container, false);
        view.setBackgroundColor(Color.WHITE);

        Button wifiSetup = view.findViewById(R.id.wifiSetupBtn);

        wifiSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.wifiSetupBtn ) {
                    //Toast.makeText(getActivity().getApplicationContext(), "yay", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), WifiSetupActivity.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

}
