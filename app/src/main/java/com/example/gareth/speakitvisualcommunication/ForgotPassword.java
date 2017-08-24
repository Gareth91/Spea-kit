package com.example.gareth.speakitvisualcommunication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gareth.speakitvisualcommunication.volley.ErrorResponse;
import com.example.gareth.speakitvisualcommunication.volley.VolleyCallBack;
import com.example.gareth.speakitvisualcommunication.volley.VolleyHelp;
import com.example.gareth.speakitvisualcommunication.volley.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {

    EditText userEmail, userName;
    TextView information;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Set back button in the bar at the top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        userEmail = (EditText) findViewById(R.id.editText8);
        userName = (EditText) findViewById(R.id.editText9);
        information = (TextView) findViewById(R.id.textView);
        submit = (Button) findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // String BASE_URL = "http://10.0.2.2:5000/project/getPassword";
                String BASE_URL = "http://awsandroid-env.gxjm8mxvzx.eu-west-1.elasticbeanstalk.com/project/getPassword";
                String url = BASE_URL;

                HashMap<String, String> headers  = new HashMap<>();
                HashMap<String, String> body  = new HashMap<>();

                body.put("email", userEmail.getText().toString());
                body.put("username", userName.getText().toString());



                String contentType =  "application/json";
                VolleyRequest request =   new VolleyRequest(ForgotPassword.this, VolleyHelp.methodDescription.POST, contentType, url, headers, body);

                request.serviceJsonCall(new VolleyCallBack(){
                    @Override
                    public void onSuccess(String result){
                        System.out.print("CALLBACK SUCCESS: " + result);

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(result);
                            String dbPassword = jsonObject.getString("password");
                            String dbUsername = jsonObject.getString("username");

                                //Getting content for email
                                String email = userEmail.getText().toString().trim();
                                String subject = "Password Recovery";
                                String message = "Hi " + dbUsername + " !" + "\n\n\n" +
                                        "Your Password is: "+dbPassword+" ";

                                //Creating SendMail object
                                SendMail sm = new SendMail(ForgotPassword.this, email, subject, message);

                                //Executing sendmail to send email
                                sm.execute();


//                            Log.i("Send email", "");
//
//                            String[] TO = {userEmail.getText().toString()};
//                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                            emailIntent.setData(Uri.parse("mailto:"));
//                            emailIntent.setType("text/plain");
//
//
//                            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Password Recovery");
//                            // Need to get data from database based on user email
//                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Your password is: "+ dbPassword +" . ");
//
//                            try {
//                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                                finish();
//                            } catch (android.content.ActivityNotFoundException ex) {
//                                Toast.makeText(ForgotPassword.this,
//                                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
//                            }



                        }catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onError(ErrorResponse errorResponse){
                        System.out.print("CALLBACK ERROR: " + errorResponse.getMessage());
                        Toast.makeText(ForgotPassword.this, "Invalid email or username", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

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

