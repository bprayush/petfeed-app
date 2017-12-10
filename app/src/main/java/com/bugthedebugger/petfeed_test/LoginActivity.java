package com.bugthedebugger.petfeed_test;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Context loginContext = this;
    private TextView testView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEt = findViewById(R.id.emailEt);
        final EditText passwordEt = findViewById(R.id.passwordEt);

        String email;
        String password;
        testView = findViewById(R.id.testText);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

        Button loginButton = findViewById(R.id.loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( view.getId() == R.id.loginBtn ) {
                    if ( !verifyEmail(emailEt.getText().toString()) || !verifyPassword(passwordEt.getText().toString()) )
                        Toast.makeText(loginContext, "Invalid email or password.", Toast.LENGTH_SHORT).show();
                    else
                    {
                        new HttpHandler().execute("http://192.168.100.236:8000/test");
                    }
                }
            }
        });


    }

    private boolean verifyEmail(String email){
        if ( email.contains("@") )
                return true;
        else
            return false;
    }

    private boolean verifyPassword(String password){
        if ( password.length() < 4 )
            return false;
        else
            return true;
    }


    public class HttpHandler extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            URL url = null;
            InputStream stream = null;
            BufferedReader reader = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String response = buffer.toString();

                JSONObject jsonObject = new JSONObject(response);

                return buffer.toString();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {

                if ( connection != null )
                    connection.disconnect();

                try {
                    if( reader != null )
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            testView.setText(s);
            progressBar.setVisibility(View.GONE);
        }
    }

}
