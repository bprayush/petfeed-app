package com.bugthedebugger.petfeed_test;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by bprayush on 12/17/17.
 */

public class GlobalRequest extends AsyncTask<String, Void, Void> {

    Context context;

    public GlobalRequest(Context context){
        this.context = context;
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

        requestQueue.add(stringRequest);

        return null;
    }
}
