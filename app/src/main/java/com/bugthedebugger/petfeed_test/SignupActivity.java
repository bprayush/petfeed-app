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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private Context signupContext = this;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar = new ProgressDialog(this);

        final EditText name = findViewById(R.id.nameEt);
        final EditText email = findViewById(R.id.semailEt);
        final EditText pwd = findViewById(R.id.spasswordEt);
        final EditText rePwd = findViewById(R.id.repasswordEt);
        final EditText address = findViewById(R.id.locationEt);
        final EditText pet = findViewById(R.id.petEt);

        Button submitBtn = findViewById(R.id.submitBt);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int flag = 0;
                if( view.getId() == R.id.submitBt )
                {
                    if ( validateName(name.getText().toString()) &&  validateEmail(email.getText().toString())
                            && validateAddress(address.getText().toString()) && validatePet(pet.getText().toString()))
                    {
                        flag = 1;
                    }
                    else {
                        Toast.makeText(signupContext, "One or more fields can't be left empty.", Toast.LENGTH_SHORT).show();
                    }

                    if ( flag == 1 )
                    {
                        if( validatePwd(pwd.getText().toString(), rePwd.getText().toString()) ){
                            String registerUrl = "https://prayush.karkhana.asia/test/register?"+"email="+
                                    email.getText().toString()+"&password="+pwd.getText().toString()+
                                    "&name="+name.getText().toString()+"&pet="+pet.getText().toString()+
                                    "&address="+address.getText().toString();
                            new SignUpHandler().execute(registerUrl);
                        }
                        else{
                            Toast.makeText(signupContext, "Passwords do not match",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

    }

    private boolean validateName(String name){
        if( name.length() <= 0 )
            return false;
        else
            return true;
    }

    private boolean validateEmail(String email){
        return email.contains("@");
    }

    private boolean validatePwd(String pwd, String rePwd){
        return !pwd.isEmpty() && pwd.length() > 4 && Objects.equals(pwd, rePwd);
    }

    private boolean validateAddress(String address){

        if ( address.length() < 4 )
            return false;
        else
            return true;
    }

    private boolean validatePet(String pet)
    {
        if( pet.length() > 2 )
            return true;
        else
            return false;
    }

    public class SignUpHandler extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {

            RequestQueue requestQueue = Volley.newRequestQueue(signupContext);
            String url = strings[0];

            final JSONObject[] tempObject = new JSONObject[1];

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("Prayush", response);
                                tempObject[0] = new JSONObject(response);

                                String status = tempObject[0].getString("status");
                                String message = tempObject[0].getString("message");

                                if (status.equals("success")) {
                                    String name = tempObject[0].getString("name");
                                    String email = tempObject[0].getString("email");
                                    String pet = tempObject[0].getString("pet");
                                    int userId = tempObject[0].getInt("id");

                                    Intent intent = new Intent(signupContext, PetfeedActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("id", userId);
                                    intent.putExtra("pet", pet);

                                    startActivity(intent);
                                    Toast.makeText(signupContext, "Registered successfully.", Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(signupContext, message, Toast.LENGTH_LONG).show();
                                    progressBar.dismiss();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d("prayush", e.getMessage());
                                // Toast.makeText(signupContext, e.getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.dismiss();
                    Toast.makeText(signupContext, "Trouble accessing the internet, check you network connection", Toast.LENGTH_LONG
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
