package com.bugthedebugger.petfeed_test;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by bprayush on 12/23/17.
 */

public class JsonRequest extends AsyncTask<String, Void, Void> {

    private JSONObject jsonData;
    private Context context;

    public JsonRequest(Context context, JSONObject jsonData){
        this.jsonData = jsonData;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {

        String url = strings[0];

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                url, jsonData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        return null;
    }
}
