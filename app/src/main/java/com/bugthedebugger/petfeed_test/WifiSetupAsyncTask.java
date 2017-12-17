package com.bugthedebugger.petfeed_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bprayush on 12/16/17.
 */

public class WifiSetupAsyncTask extends AsyncTask<String, Void, Void> {

    Context context;
    ProgressDialog progressDialog;

    public WifiSetupAsyncTask(Context context, ProgressDialog progressDialog){
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(String... url) {


        RequestQueue requestQueue = Volley.newRequestQueue(this.context);

        String petfeedUrl = url[0];

        StringRequest stringRequest = new StringRequest(Request.Method.GET, petfeedUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if( status.equals("success") )
                    {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        ((Activity)context).finish();
                    }
                    else if( status.equals("error") )
                    {
                        String message = jsonObject.getString("message");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        progressDialog.dismiss();
        requestQueue.add(stringRequest);

        return null;
    }
}
