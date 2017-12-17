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


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class GlobalFeedingFragment extends Fragment {

    String status;
    String email;
    int id;

    @SuppressLint("ValidFragment")
    public GlobalFeedingFragment(String status, String email, int id) {
        // Required empty public constructor
        this.status = status;
        this.email = email;
        this.id = id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_global_feeding, container, false);
        view.setBackgroundColor(Color.WHITE);

        TextView globalStatus = view.findViewById(R.id.globalStatusTv);

        if( status.equals("offline") )
        {
            globalStatus.setText(status);
            globalStatus.setTextColor(Color.RED);
        }
        else if( status.equals("online") )
        {
            globalStatus.setText(status);
            globalStatus.setTextColor(Color.GREEN);
        }


        Button treatBtn = view.findViewById(R.id.globalTreatBtn);

        treatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.globalTreatBtn )
                {
                    String treatUrl = "https://prayush.karkhana.asia/test/treat?email="+
                            email+"&id="+id;
                    new GlobalRequest(getActivity().getApplicationContext()).execute(treatUrl);
                }
            }
        });

        return view;
    }

}
