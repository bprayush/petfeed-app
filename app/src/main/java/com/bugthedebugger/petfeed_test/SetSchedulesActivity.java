package com.bugthedebugger.petfeed_test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetSchedulesActivity extends AppCompatActivity {

    Context context = this;
    String email;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedules);

        email = getIntent().getStringExtra("user");
        id = getIntent().getIntExtra("id", 0);
        final String[] days = {
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        };


        final EditText sundayMet = findViewById(R.id.sundaySetScheduleMTime);
        final EditText sundayEet = findViewById(R.id.sundaySetScheduleETime);
        final EditText mondayMet = findViewById(R.id.mondaySetScheduleMTime);
        final EditText mondayEet = findViewById(R.id.mondaySetScheduleETime);
        final EditText tuesdayMet = findViewById(R.id.tuesdaySetScheduleMTime);
        final EditText tuesdayEet = findViewById(R.id.tuesdaySetScheduleETime);
        final EditText wednesdayMet = findViewById(R.id.wednesdaySetScheduleMTime);
        final EditText wednesdayEet = findViewById(R.id.wednesdaySetScheduleETime);
        final EditText thursdayMet = findViewById(R.id.thursdaySetScheduleMTime);
        final EditText thursdayEet = findViewById(R.id.thursdaySetScheduleETime);
        final EditText fridayMet = findViewById(R.id.fridaySetScheduleMTime);
        final EditText fridayEet = findViewById(R.id.fridaySetScheduleETime);
        final EditText saturdayMet = findViewById(R.id.saturdaySetScheduleMTime);
        final EditText saturdayEet = findViewById(R.id.saturdaySetScheduleETime);
        final CheckBox overrideBox = findViewById(R.id.overrideExistingCB);

        Button saveBtn = findViewById(R.id.saveScheduleBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.saveScheduleBtn )
                {
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
                    JSONObject requestObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    JSONObject tempObject = new JSONObject();

                    for( int i=0; i<7; i++ ){

                        switch(i){

                            case 0:
                                try {
                                    String sundayM = sundayMet.getText().toString();
                                    String sundayE = sundayEet.getText().toString();
                                    if (!sundayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", sundayM);
                                        jsonArray.put(tempObject);
                                        Log.d("prayush", "sunday1");
                                    } else if (!sundayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", sundayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 1:
                                try{
                                    String mondayM = mondayMet.getText().toString();
                                    String mondayE = mondayEet.getText().toString();
                                    if (!mondayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", mondayM);
                                        jsonArray.put(tempObject);
                                    } else if (!mondayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", mondayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 2:
                                try{
                                    String tuesdayM = tuesdayMet.getText().toString();
                                    String tuesdayE = tuesdayEet.getText().toString();
                                    if (!tuesdayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", tuesdayM);
                                        jsonArray.put(tempObject);
                                    } else if (!tuesdayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", tuesdayM);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 3:
                                try{
                                    String wednesdayM = wednesdayMet.getText().toString();
                                    String wednesdayE = wednesdayEet.getText().toString();

                                    if (!wednesdayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", wednesdayM);
                                        jsonArray.put(tempObject);
                                    } else if (!wednesdayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", wednesdayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 4:
                                try{
                                    String thursdayM = thursdayMet.getText().toString();
                                    String thursdayE = thursdayEet.getText().toString();

                                    if (!thursdayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", thursdayM);
                                        jsonArray.put(tempObject);
                                    } else if (!thursdayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", thursdayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 5:
                                try{
                                    String fridayM = fridayMet.getText().toString();
                                    String fridayE = fridayEet.getText().toString();

                                    if (!fridayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", fridayM);
                                        jsonArray.put(tempObject);
                                    } else if (!fridayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", fridayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                            case 6:
                                try{
                                    String saturdayM = saturdayMet.getText().toString();
                                    String saturdayE = saturdayEet.getText().toString();

                                    if (!saturdayM.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", saturdayM);
                                        jsonArray.put(tempObject);
                                    } else if (!saturdayE.isEmpty()) {
                                        tempObject.put("day", days[i]);
                                        tempObject.put("time", saturdayE);
                                        jsonArray.put(tempObject);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                break;

                        }

                    }

                    try {
                        requestObject.put("email", email);
                        requestObject.put("id", id);
                        requestObject.put("data", jsonArray);
                        int checked = 0;
                        if( overrideBox.isChecked() )
                            checked = 1;
                        else
                            checked = 0;
                        requestObject.put("update", checked);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new JsonRequest(context, requestObject).execute("https://prayush.karkhana.asia/test/schedule/set");
                    jsonArray = null;
                    requestObject = null;
                    tempObject = null;
                }
            }
        });

    }
}
