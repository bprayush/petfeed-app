package com.bugthedebugger.petfeed_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by bprayush on 12/15/17.
 */

public class GlobalDevice extends AsyncTask<String, Void, Void> {


    Context context;
    ProgressDialog progressDialog;

    public GlobalDevice(Context context, ProgressDialog progressDialog){
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        progressDialog.dismiss();
        requestQueue.add(stringRequest);

        return null;
    }
}