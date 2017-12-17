package com.bugthedebugger.petfeed_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class WifiSetupActivity extends AppCompatActivity {

    Context wifiContext = this;
    String local_device_status;
    String connection_method;
    String piIpAddress;
    Boolean hasPiIpAddress;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setup);

        hasPiIpAddress = false;
        progressDialog = new ProgressDialog(wifiContext);


        final EditText ssidEt = findViewById(R.id.ssidEt);
        final EditText keyEt = findViewById(R.id.keyEt);

        Button setWifiBtn = findViewById(R.id.setDeviceWifiBtn);

        setWifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( view.getId() == R.id.setDeviceWifiBtn ) {
                    String ssid = ssidEt.getText().toString();
                    String key = keyEt.getText().toString();

                    final String wifiUrl = "/wifisetup?ssid="+ssid+"&key="+key;
                    new IpScanner().execute(wifiUrl);
                }
            }
        });


    }

    public class IpScanner extends AsyncTask<String, Void, Void> {

        InetAddress getWLANipAddress(String protocolVersion) throws SocketException {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                if (netint.isUp() && !netint.isLoopback() && !netint.isVirtual()) {
                    Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
                    for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                        if (protocolVersion.equals("IPv4")) {
                            if (inetAddress instanceof Inet4Address) {
                                return inetAddress;
                            }
                        } else {
                            if (inetAddress instanceof Inet6Address) {
                                return inetAddress;
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected Void doInBackground(String... urls) {

            final String wifiUrl = urls[0];

            try {
                String clientIp = String.valueOf(getWLANipAddress("IPv4"));
                clientIp = clientIp.substring(0, clientIp.lastIndexOf("."));
                clientIp = clientIp.replace("/", "");
                Log.d("prayuship", clientIp);

                List<String> reachableHosts = new ArrayList<String>();

                int timeout=10;
                for (int i=2;i<255;i++){
                    String host=clientIp + "." + i;
                    Log.d("prayuship", host);
                    if (InetAddress.getByName(host).isReachable(timeout)){
                        Log.d("prayuship", host + " is reachable");
                        reachableHosts.add(host);
                    }
                }

                final RequestQueue requestQueue = Volley.newRequestQueue(wifiContext);


                //Log.d("prayush", String.valueOf(reachableHosts.size()));
                for(int i=0; i<reachableHosts.size(); i++)
                {
                    final String host = reachableHosts.get(i);
                    //Log.d("prayush", host);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + host,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Log.d("prayush", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        String d_status = jsonObject.getString("status");
                                        String d_connection = jsonObject.getString("connection");

                                        if (d_status.equals("online")) {
                                            local_device_status = d_status;
                                            connection_method = d_connection;
                                            piIpAddress = host;
                                            hasPiIpAddress = true;
                                            Log.d("prayuship", "Found my pi");
                                            Log.d("prayuship", "Inside hasIpAddress");
                                            StringRequest piRequest = new StringRequest(Request.Method.GET, "http://" +
                                                    piIpAddress + wifiUrl, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {

                                                        Log.d("prayuship", response);

                                                        JSONObject jsonObject = new JSONObject(response);

                                                        String status = jsonObject.getString("status");
                                                        String message = jsonObject.getString("message");

                                                        if( status.equals("success") )
                                                        {
                                                            Toast.makeText(wifiContext, message, Toast.LENGTH_SHORT).show();
                                                            //Log.d("prayuship", "inside success");
                                                        }
                                                        else if( status.equals("error") )
                                                        {
                                                            Toast.makeText(wifiContext, message, Toast.LENGTH_SHORT).show();
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            });
                                            requestQueue.add(piRequest);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                    requestQueue.add(stringRequest);
                }
                progressDialog.dismiss();

            } catch (UnknownHostException e) {
                e.printStackTrace();
                //Log.d("prayuship", "a");
            } catch (SocketException e) {
                e.printStackTrace();
                //Log.d("prayuship", "b");
            } catch (IOException e) {
                e.printStackTrace();
                //Log.d("prayuship", "c");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Setting up device wifi, please wait.");
            progressDialog.show();
            super.onPreExecute();
        }

    }

}
