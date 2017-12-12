package com.bugthedebugger.petfeed_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Context loginContext = this;
    TextView testView;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEt = findViewById(R.id.emailEt);
        final EditText passwordEt = findViewById(R.id.passwordEt);

        testView = findViewById(R.id.testText);
        progressBar = new ProgressDialog(loginContext);

        final Button loginButton = findViewById(R.id.loginBtn);
        final Button signupButton = findViewById(R.id.signupBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.loginBtn ) {
                    if ( !verifyEmail(emailEt.getText().toString()) || !verifyPassword(passwordEt.getText().toString()) )
                        Toast.makeText(loginContext, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    else
                    {
                        String email = emailEt.getText().toString();
                        String password = passwordEt.getText().toString();
                        new HttpHandler().execute("https://prayush.karkhana.asia/test/login?email="+email+"&password="+password);
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.signupBtn )
                {
//                    Intent intent = new Intent(loginContext, SignupActivity.class);
//                    startActivity(intent);
//
//                    finish();
                    Toast.makeText(loginContext, "SIGN UP", Toast.LENGTH_LONG).show();
                    Intent intent  = new Intent(LoginActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    private boolean verifyEmail(String email){
        return email.contains("@");
    }

    private boolean verifyPassword(String password){
        return password.length() >= 4;
    }


    public class HttpHandler extends AsyncTask<String, Void, Void>{


        @Override
        protected Void doInBackground(String... urls) {
            RequestQueue requestQueue = Volley.newRequestQueue(loginContext);
            String url = urls[0];

            final JSONObject[] tempObject = new JSONObject[1];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                tempObject[0] = new JSONObject(response);

                                String status = tempObject[0].getString("status");
                                String message = tempObject[0].getString("message");


                                if ( status.equals("success") ) {
                                    String userName = tempObject[0].getString("name");
                                    String email = tempObject[0].getString("email");
                                    String pet = tempObject[0].getString("pet");
                                    int userId = tempObject[0].getInt("id");

                                    Intent intent = new Intent(loginContext, PetfeedActivity.class);
                                    intent.putExtra("name", userName);
                                    intent.putExtra("email", email);
                                    intent.putExtra("id", userId);
                                    intent.putExtra("pet", pet);

                                    startActivity(intent);
                                    Toast.makeText(loginContext, "Login successful.", Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();
                                    finish();
                                }
                                else {
                                    Toast.makeText(loginContext, message, Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(loginContext, e.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.dismiss();
                            }
                            // Log.d("prayush", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Log.d("prayush", error.toString() );
                    progressBar.dismiss();
                    Toast.makeText(loginContext, "Trouble accessing the internet, check you network connection", Toast.LENGTH_LONG
                    ).show();
                }

            });

            requestQueue.add(stringRequest);
            return null;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setMessage("Processing please wait.");
            progressBar.show();

            super.onPreExecute();
        }

    }

}
