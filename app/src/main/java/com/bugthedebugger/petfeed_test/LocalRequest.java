package com.bugthedebugger.petfeed_test;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bprayush on 12/19/17.
 */

public class LocalRequest extends AsyncTask<String, Void, Void> {

    Context context;

    public LocalRequest(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... urls) {

        String petFeedUrl = urls[0];

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, petFeedUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            String message = jsonObject.getString("message");

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error occured while feeding your pet.",
                                Toast.LENGTH_SHORT).show();
                        //Log.d("prayush", error.getMessage());
                    }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0,
                0));
        requestQueue.add(stringRequest);
        return null;
    }
}
