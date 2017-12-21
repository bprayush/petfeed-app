package com.bugthedebugger.petfeed_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


public class PetfeedActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context petfeedContext = this;
    String local_device_status;
    String global_device_status;
    String connection_method;
    String piIpAddress;
    String petName;
    ProgressDialog progressDialog;
    String email;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petfeed);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        piIpAddress="";

        progressDialog = new ProgressDialog(petfeedContext);

        local_device_status = "offline";
        global_device_status = "offline";

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        userId = getIntent().getIntExtra("id", 0);
        petName = getIntent().getStringExtra("pet");

        View headerLayout = navigationView.getHeaderView(0);
        TextView userEmail = headerLayout.findViewById(R.id.userEmail);
        TextView userName = headerLayout.findViewById(R.id.userName);

        userEmail.setText(email);
        userName.setText(name);


        final String channel_name = "petfeed";
        final String event_name = "App\\Events\\eventTrigger";
        // Log.d("prayush", event_name);

        Pusher pusher = new Pusher("0053280ec440a78036bc");
        Channel channel = pusher.subscribe(channel_name);

        channel.bind(event_name, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(data);

                            String gstatus = jsonObject.getString("status");
                            //  Log.d("prayush", global_device_status);
                            if (gstatus.equals("online"))
                            {
                                global_device_status = gstatus;
                                String message = jsonObject.getString("message");
                                Toast.makeText(petfeedContext, message, Toast.LENGTH_SHORT).show();
                            }
                            else if( gstatus.equals("error") )
                            {
                                String message = jsonObject.getString("message");
                                Toast.makeText(petfeedContext, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Log.d("prayush", "Got and exception");
                        }
                    }
                });

            }
        });

        pusher.connect();

        String petFeedConnectUrl = "https://prayush.karkhana.asia/test/get/status?email="
                +email+"&id="+String.valueOf(userId);

        new IpScanner().execute();
        new GlobalRequestWithProgress(this, progressDialog).execute(petFeedConnectUrl);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_content_layout, new WelcomeFragment(petName));
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.device_status_nv) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content_layout, new StatusFragment(local_device_status, global_device_status));
            ft.commit();
        } else if (id == R.id.device_setup) {
            //Toast.makeText(petfeedContext, "Setup clicked.", Toast.LENGTH_LONG).show();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.fragment_content_layout, new SetupFragment());
            ft.commit();
        } else if (id == R.id.nav_global) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content_layout, new GlobalFeedingFragment(global_device_status, email, userId));
            ft.commit();
        } else if (id == R.id.nav_local) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content_layout, new LocalFeedingFragment(local_device_status, piIpAddress));
            ft.commit();
        } else if (id == R.id.nav_send) {

        } else if (id == R.id.petfeed_home_nv){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_content_layout, new WelcomeFragment(petName));
            ft.commit();
        } else if (id == R.id.restart){
            if( !piIpAddress.isEmpty() ){
                String restartUrl = "http://"+piIpAddress+"/restart";
                new GlobalRequest(petfeedContext).execute(restartUrl);
            }
            else{
                Toast.makeText(petfeedContext, "Could not find device in local network.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.power_off){
            if( !piIpAddress.isEmpty() ){
                String shutdownUrl = "http://"+piIpAddress+"/shutdown";
                new GlobalRequest(petfeedContext).execute(shutdownUrl);
            }
            else{
                Toast.makeText(petfeedContext, "Could not find device in local network.", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                clientIp = clientIp.substring(0, clientIp.lastIndexOf("."));
                clientIp = clientIp.replace("/", "");
                 Log.d("prayuship", clientIp);

                 List<String> reachableHosts = new ArrayList<String>();
                RequestQueue requestQueue = Volley.newRequestQueue(petfeedContext);

                int timeout=10;
                for (int i=2;i<255;i++){
                    final String host=clientIp + "." + i;
                    Log.d("prayuship", host);
                    if (InetAddress.getByName(host).isReachable(timeout)){
                         Log.d("prayuship", host + " is reachable");
                        reachableHosts.add(host);
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + host,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Log.d("prayush", response);
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        Log.d("prayush", response);
                                        String d_status = jsonObject.getString("status");
                                        String d_connection = jsonObject.getString("connection");

                                        if (d_status.equals("online")) {
                                            local_device_status = d_status;
                                            connection_method = d_connection;
                                            piIpAddress = host;
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
                    if( !piIpAddress.isEmpty() )
                        break;
                }


                //Log.d("prayush", String.valueOf(reachableHosts.size()));
//                for(int i=0; i<reachableHosts.size(); i++)
//                {
//                    final String host = reachableHosts.get(i);
//                    //Log.d("prayush", host);
//                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + host,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                    //Log.d("prayush", response);
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//
//                                        String d_status = jsonObject.getString("status");
//                                        String d_connection = jsonObject.getString("connection");
//
//                                        if (d_status.equals("online")) {
//                                            local_device_status = d_status;
//                                            connection_method = d_connection;
//                                            piIpAddress = host;
//                                        }
//
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//
//                            }
//                    });
//                    requestQueue.add(stringRequest);
//                }
                //piIpAddress = local;
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
    }

}
