package com.bugthedebugger.petfeed_test;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;


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
        Button setScheduleBtn = view.findViewById(R.id.setScheduleBtn);

        treatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.globalTreatBtn ) {
                    if (status.equals("online")) {
                        String treatUrl = "https://prayush.karkhana.asia/test/treat?email=" +
                                email + "&id=" + id;
                        new GlobalRequest(getActivity().getApplicationContext()).execute(treatUrl);

                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Looks like your device is offline.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.setScheduleBtn ){
                    Intent intent = new Intent(getActivity().getApplicationContext(),
                            SetSchedulesActivity.class);
                    intent.putExtra("user", email);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    /*
                    Log.d("prayush", "Inside set schedule");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject();
                        jsonObject.put("data", "prayush");
                        jsonObject.put("email", email);
                        jsonObject.put("id", id);
//                        new JsonRequest(getActivity().getApplicationContext(),
//                                jsonObject).execute("https://prayush.karkhana.asia/test/schedule/set");
                        new JsonRequest(getActivity().getApplicationContext(),
                                jsonObject).execute("http://192.168.100.236:8000/test/schedule/set");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Looks like your device is offline.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

}
