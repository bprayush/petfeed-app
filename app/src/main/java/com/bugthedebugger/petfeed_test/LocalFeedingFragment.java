package com.bugthedebugger.petfeed_test;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class LocalFeedingFragment extends Fragment {

    String status;
    String piIpAddress;

    @SuppressLint("ValidFragment")
    public LocalFeedingFragment(String status, String piIpAddress) {
        // Required empty public constructor
        this.status = status;
        this.piIpAddress = piIpAddress;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_feeding, container, false);

        view.setBackgroundColor(Color.WHITE);

        TextView localStatus = view.findViewById(R.id.localFeedStatusTv);

        if (status.equals("online")){
            localStatus.setTextColor(Color.GREEN);
            localStatus.setText(status);
        }else if(status.equals("offline")){
            localStatus.setTextColor(Color.RED);
            localStatus.setText(status);
        }

        Button treatBtn = view.findViewById(R.id.localTreatBtn);
        treatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.localTreatBtn ){
                    if( status.equals("online") )
                    {
                        String url = "http://"+piIpAddress+"/feed";
                        new LocalRequest(getActivity().getApplicationContext()).execute(url);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Feeding your pet please wait", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Could not find device in local network.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

}
