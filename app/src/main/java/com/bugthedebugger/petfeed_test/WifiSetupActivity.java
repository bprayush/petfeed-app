package com.bugthedebugger.petfeed_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        String ssid;
        String key;

        EditText ssidEt = findViewById(R.id.ssidEt);
        EditText keyEt = findViewById(R.id.keyEt);

        ssid = ssidEt.getText().toString();
        key = keyEt.getText().toString();

        Button setWifiBtn = findViewById(R.id.setDeviceWifiBtn);

        setWifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( view.getId() == R.id.setDeviceWifiBtn )
                    new IpScanner().execute();
            }
        });



        String wifiUrl = "/wifisetup?ssid="+ssid+"&key="+key;

        if( hasPiIpAddress ) {
            String setUpUrl = "http://"+piIpAddress+wifiUrl;
            new WifiSetupAsyncTask(wifiContext, progressDialog).execute();
        }

    }

    public class IpScanner extends AsyncTask<Void, Void, Void> {

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
        protected Void doInBackground(Void... voids) {

            try {
                String clientIp = String.valueOf(getWLANipAddress("IPv4"));
                //clientIp = clientIp.substring(0, clientIp.lastIndexOf("."));
                //Log.d("prayuship", clientIp.replace("/", ""));
                //Log.d("prayuship", String.valueOf(InetAddress.getByName("192.168.100.1").isReachable(10)));

                List<String> reachableHosts = new ArrayList<String>();

                int timeout=10;
                for (int i=2;i<255;i++){
                    String host="192.168.100" + "." + i;
                    if (InetAddress.getByName(host).isReachable(timeout)){
                        //Log.d("prayuship", host + " is reachable");
                        reachableHosts.add(host);
                    }
                }

                RequestQueue requestQueue = Volley.newRequestQueue(wifiContext);


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
            progressDialog.setMessage("Searching for device, please wait.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if( !hasPiIpAddress )
                Toast.makeText(wifiContext, "No device found.", Toast.LENGTH_SHORT).show();
        }
    }

}
