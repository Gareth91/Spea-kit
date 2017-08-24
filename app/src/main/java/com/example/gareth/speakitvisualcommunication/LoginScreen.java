package com.example.gareth.speakitvisualcommunication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gareth.speakitvisualcommunication.volley.ErrorResponse;
import com.example.gareth.speakitvisualcommunication.volley.VolleyCallBack;
import com.example.gareth.speakitvisualcommunication.volley.VolleyHelp;
import com.example.gareth.speakitvisualcommunication.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by Connaire on 05/08/2017.
 * Modified by Gareth, Anthony and Ashley on 22/08/2017
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    EditText Username, Password;
    Button Login;
    Button Forgot;
    Button Register;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Login");


        Login = (Button) findViewById(R.id.button3);
        Username = (EditText) findViewById(R.id.editText6);
        Password = (EditText) findViewById(R.id.editText7);
        Forgot = (Button) findViewById(R.id.forgotbutton);
        Register = (Button) findViewById(R.id.button4);

        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        Forgot.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button3:
                if (Username.getText().toString().equals("")) {
                    Username.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else if (Password.getText().toString().equals("")) {
                    Password.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                } else {
                    Username.getBackground().clearColorFilter();
                    Password.getBackground().clearColorFilter();
                }
                //String BASE_URL = "http://awsandroid.eu-west-1.elasticbeanstalk.com/project/getAccountDetails";
                //String BASE_URL = "http://10.0.2.2:5000/project/getAccountDetails";
                String BASE_URL = "http://awsandroid-env.gxjm8mxvzx.eu-west-1.elasticbeanstalk.com/project/getAccountDetails";
                String url = BASE_URL;

                HashMap<String, String> headers = new HashMap<>();
                HashMap<String, String> body = new HashMap<>();

                body.put("username", Username.getText().toString());
                body.put("password", Password.getText().toString());


                String contentType = "application/json";
                VolleyRequest request = new VolleyRequest(LoginScreen.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                request.serviceJsonCall(new VolleyCallBack() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.print("CALLBACK SUCCESS: " + result);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(result);
                            String dbUsername = jsonObject.getString("username");
                            String dbPassword = jsonObject.getString("password");

                            if (Username.getText().toString().equals(dbUsername) && Password.getText().toString().equals(dbPassword) ) {

                                Toast.makeText(LoginScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                DataHolder.getInstance().setLogin(dbUsername);
                                Intent intent = new Intent(LoginScreen.this, UserSelect.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginScreen.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());

                    }
                });
                break;
            case R.id.button4:
                Intent registerIntent = new Intent(LoginScreen.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.forgotbutton:
                Intent forgotIntent = new Intent(LoginScreen.this, ForgotPassword.class);
                startActivity(forgotIntent);
                break;
            default:
                break;
        }
    }

    
    /**
     * Method for the selection of the home button
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


